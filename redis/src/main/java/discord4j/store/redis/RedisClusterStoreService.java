/*
 * This file is part of Discord4J.
 *
 * Discord4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Discord4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Discord4J. If not, see <http://www.gnu.org/licenses/>.
 */

package discord4j.store.redis;

import discord4j.store.api.Store;
import discord4j.store.api.primitive.ForwardingStore;
import discord4j.store.api.primitive.LongObjStore;
import discord4j.store.api.service.StoreService;
import discord4j.store.api.util.StoreContext;
import io.lettuce.core.RedisClient;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.codec.RedisCodec;
import reactor.core.publisher.Mono;

import static discord4j.store.redis.RedisStoreDefaults.*;

/**
 * A {@link StoreService} implementation that creates {@link RedisStore} instances capable of communicating to a
 * Redis server through Lettuce Core driver using a single stateful connection.
 * <p>
 * This factory can be created under a number of configurations, particularly allowing customization of Lettuce
 * {@link RedisClusterClient}, the codec used to serializer and deserialize entities and the key prefix used when saving
 * entities.
 * <p>
 * For a standalone redis connection, use {@link RedisStoreService}.
 *
 * @see <a href="https://lettuce.io/">Lettuce Core</a>
 */
public class RedisClusterStoreService implements StoreService {

    private final RedisClusterClient client;
    private final StatefulRedisClusterConnection<byte[], byte[]> connection;
    private final RedisSerializerFactory keySerializerFactory;
    private final RedisSerializerFactory valueSerializerFactory;
    private final String keyPrefix;
    private final boolean sharedConnection;
    private final RedisCodec<byte[], byte[]> redisCodec;

    /**
     * Create a {@link RedisClusterStoreService} with all defaults for usage with Discord4J data entities.
     */
    public RedisClusterStoreService() {
        this(defaultClusterClient(), byteArrayCodec(), keyPrefix(),
            stringKeySerializerFactory(), jacksonValueSerializerFactory(), true);
    }

    /**
     * Create a {@link RedisClusterStoreService} with the given parameters. To use defaults and individually change
     * parameters, use {@link #builder()}.
     *
     * @param redisClient            the backing Lettuce's {@link RedisClusterClient}
     * @param redisCodec             a raw byte array {@link RedisCodec} to encode and decode redis commands
     * @param keyPrefix              a prefix used for each structure created by this store
     * @param keySerializerFactory   a factory to derive serializers depending on the store key type
     * @param valueSerializerFactory a factory to derive serializers depending on the stored value type
     */
    public RedisClusterStoreService(RedisClusterClient redisClient, RedisCodec<byte[], byte[]> redisCodec,
                                    String keyPrefix,
                                    RedisSerializerFactory keySerializerFactory,
                                    RedisSerializerFactory valueSerializerFactory,
                                    boolean sharedConnection) {
        this.client = redisClient;
        this.keySerializerFactory = keySerializerFactory;
        this.valueSerializerFactory = valueSerializerFactory;
        this.keyPrefix = keyPrefix;
        this.sharedConnection = sharedConnection;
        if (sharedConnection) {
            this.redisCodec = null;
            this.connection = client.connect(redisCodec);
        } else {
            this.redisCodec = redisCodec;
            this.connection = null;
        }
    }

    /**
     * Get a builder to construct {@link RedisStore} instances.
     *
     * @return a builder
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean hasGenericStores() {
        return true;
    }

    @Override
    public <K extends Comparable<K>, V> Store<K, V> provideGenericStore(Class<K> keyClass, Class<V> valueClass) {
        return new RedisStore<>(sharedConnection ? connection : client.connect(redisCodec),
            keyPrefix + valueClass.getSimpleName(),
            keySerializerFactory.create(keyClass), valueSerializerFactory.create(valueClass));
    }

    @Override
    public boolean hasLongObjStores() {
        return true;
    }

    @Override
    public <V> LongObjStore<V> provideLongObjStore(Class<V> valueClass) {
        return new ForwardingStore<>(provideGenericStore(Long.class, valueClass));
    }

    @Override
    public void init(StoreContext context) {
    }

    @Override
    public Mono<Void> dispose() {
        return Mono.defer(() -> Mono.fromFuture(client.shutdownAsync()));
    }

    /**
     * Builder for {@link RedisClient}
     */
    public static class Builder {

        private RedisClusterClient redisClient = defaultClusterClient();
        private RedisCodec<byte[], byte[]> redisCodec = byteArrayCodec();
        private String keyPrefix = RedisStoreDefaults.keyPrefix();
        private RedisSerializerFactory keySerializerFactory = jacksonValueSerializerFactory();
        private RedisSerializerFactory valueSerializerFactory = jacksonValueSerializerFactory();
        private boolean sharedConnection = true;

        public Builder() {
        }

        /**
         * Set the backing Lettuce's {@link RedisClusterClient}. Defaults to
         * {@link RedisStoreDefaults#defaultClusterClient()}.
         *
         * @param redisClient the backing redis client
         * @return this builder
         */
        public Builder redisClient(RedisClusterClient redisClient) {
            this.redisClient = redisClient;
            return this;
        }

        /**
         * Set the raw byte array {@link RedisCodec} to encode and decode redis commands. Defaults to
         * {@link RedisStoreDefaults#byteArrayCodec()}.
         *
         * @param redisCodec the codec used by the client to encode and decode commands
         * @return this builder
         */
        public Builder redisCodec(RedisCodec<byte[], byte[]> redisCodec) {
            this.redisCodec = redisCodec;
            return this;
        }

        /**
         * Set a prefix used for each structure created by this store. Defaults to
         * {@link RedisStoreDefaults#keyPrefix()}.
         *
         * @param keyPrefix the key prefix to use with the store
         * @return this builder
         */
        public Builder keyPrefix(String keyPrefix) {
            this.keyPrefix = keyPrefix;
            return this;
        }

        /**
         * Set a factory to derive serializers depending on the store key type. Defaults to
         * {@link RedisStoreDefaults#stringKeySerializerFactory()} that converts keys into a String representation.
         *
         * @param keySerializerFactory a serializer factory for keys
         * @return this builder
         */
        public Builder keySerializerFactory(RedisSerializerFactory keySerializerFactory) {
            this.keySerializerFactory = keySerializerFactory;
            return this;
        }

        /**
         * Set a factory to derive serializers depending on the stored value type. Defaults to
         * {@link RedisStoreDefaults#jacksonValueSerializerFactory()} that converts values using Jackson mapper.
         *
         * @param valueSerializerFactory a serializer factory for values
         * @return this builder
         */
        public Builder valueSerializerFactory(RedisSerializerFactory valueSerializerFactory) {
            this.valueSerializerFactory = valueSerializerFactory;
            return this;
        }

        public Builder useSharedConnection(final boolean sharedConnection) {
            this.sharedConnection = sharedConnection;
            return this;
        }

        /**
         * Create the resulting object with the properties of this builder.
         *
         * @return a {@link RedisClusterStoreService}
         */
        public RedisClusterStoreService build() {
            return new RedisClusterStoreService(redisClient, redisCodec, keyPrefix,
                keySerializerFactory, valueSerializerFactory, sharedConnection);
        }
    }
}
