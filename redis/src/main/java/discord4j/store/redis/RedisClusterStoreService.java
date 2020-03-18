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
 *
 * @see <a href="https://lettuce.io/">Lettuce Core</a>
 */
public class RedisClusterStoreService implements StoreService {

    private final RedisClusterClient client;
    private final StatefulRedisClusterConnection<byte[], byte[]> connection;
    private final RedisSerializerFactory keySerializerFactory;
    private final RedisSerializerFactory valueSerializerFactory;
    private final String keyPrefix;

    public RedisClusterStoreService() {
        this(defaultClusterClient(), byteArrayCodec(), keyPrefix(),
                stringKeySerializerFactory(), jacksonValueSerializerFactory());
    }

    public RedisClusterStoreService(RedisClusterClient redisClient, RedisCodec<byte[], byte[]> redisCodec, String keyPrefix,
                                    RedisSerializerFactory keySerializerFactory,
                                    RedisSerializerFactory valueSerializerFactory) {
        this.client = redisClient;
        this.connection = client.connect(redisCodec);
        this.keySerializerFactory = keySerializerFactory;
        this.valueSerializerFactory = valueSerializerFactory;
        this.keyPrefix = keyPrefix;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean hasGenericStores() {
        return true;
    }

    @Override
    public <K extends Comparable<K>, V> Store<K, V> provideGenericStore(Class<K> keyClass, Class<V> valueClass) {
        return new RedisStore<>(connection, keyPrefix + valueClass.getSimpleName(),
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

    public static class Builder {

        private RedisClusterClient redisClient = defaultClusterClient();
        private RedisCodec<byte[], byte[]> redisCodec = byteArrayCodec();
        private RedisSerializerFactory keySerializerFactory = jacksonValueSerializerFactory();
        private RedisSerializerFactory valueSerializerFactory = jacksonValueSerializerFactory();
        private String keyPrefix = RedisStoreDefaults.keyPrefix();

        public Builder() {
        }

        public Builder redisClient(RedisClusterClient redisClient) {
            this.redisClient = redisClient;
            return this;
        }

        public Builder redisCodec(RedisCodec<byte[], byte[]> redisCodec) {
            this.redisCodec = redisCodec;
            return this;
        }

        public Builder keySerializerFactory(RedisSerializerFactory keySerializerFactory) {
            this.keySerializerFactory = keySerializerFactory;
            return this;
        }

        public Builder valueSerializerFactory(RedisSerializerFactory valueSerializerFactory) {
            this.valueSerializerFactory = valueSerializerFactory;
            return this;
        }

        public Builder keyPrefix(String keyPrefix) {
            this.keyPrefix = keyPrefix;
            return this;
        }

        public RedisClusterStoreService build() {
            return new RedisClusterStoreService(redisClient, redisCodec, keyPrefix,
                    keySerializerFactory, valueSerializerFactory);
        }
    }
}
