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

import com.austinv11.servicer.WireService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import discord4j.discordjson.possible.PossibleFilter;
import discord4j.discordjson.possible.PossibleModule;
import discord4j.store.api.Store;
import discord4j.store.api.primitive.ForwardingStore;
import discord4j.store.api.primitive.LongObjStore;
import discord4j.store.api.service.StoreService;
import discord4j.store.api.util.StoreContext;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.RedisCodec;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.io.IOException;

/**
 * A {@link StoreService} implementation that creates {@link RedisStore} instances capable of communicating to a
 * Redis server through Lettuce Core driver using a single stateful connection.
 * <p>
 * This factory can be created under a number of configurations, particularly allowing customization of Lettuce
 * {@link RedisClient}, the codec used to serializer and deserialize entities and the key prefix used when saving
 * entities.
 *
 * @see <a href="https://lettuce.io/">Lettuce Core</a>
 */
@WireService(StoreService.class)
public class RedisStoreService implements StoreService {

    private static final Logger log = Loggers.getLogger(RedisStoreService.class);

    public static final String DEFAULT_REDIS_URI = "redis://localhost";
    public static final String DEFAULT_KEY_PREFIX = "discord4j:store:";

    private final RedisClient client;
    private final StatefulRedisConnection<String, byte[]> connection;
    private final RedisSerializerFactory valueSerializerFactory;
    private final String keyPrefix;

    public RedisStoreService() {
        this(defaultClient(), defaultCodec(), defaultSerializerFactory(), defaultKeyPrefix());
    }

    public RedisStoreService(RedisClient redisClient, RedisCodec<String, byte[]> redisCodec,
                             RedisSerializerFactory valueSerializerFactory, String keyPrefix) {
        this.client = redisClient;
        this.connection = client.connect(redisCodec);
        this.valueSerializerFactory = valueSerializerFactory;
        this.keyPrefix = keyPrefix;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Get the default client, connecting to localhost, or to a custom server given by the {@code D4J_REDIS_URL}
     * environment variable.
     *
     * @return the default lettuce-core client
     */
    public static RedisClient defaultClient() {
        String url = System.getenv("D4J_REDIS_URL");
        String redisUri = url != null ? url : DEFAULT_REDIS_URI;
        return RedisClient.create(redisUri);
    }

    /**
     * Get the default codec capable of converting between Java and redis using String serialization of keys and JSON
     * serialization of values.
     *
     * @return the default redis codec
     */
    public static RedisCodec<String, byte[]> defaultCodec() {
        return new StoreRedisCodec<>(new StringSerializer(), new ByteArraySerializer());
    }

    /**
     * Get the default factory capable of using Jackson to serialize and deserialize values in a type-safe way.
     *
     * @return a default {@link RedisSerializerFactory}
     */
    public static RedisSerializerFactory defaultSerializerFactory() {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new PossibleModule())
                .registerModule(new Jdk8Module())
                .addHandler(new DeserializationProblemHandler() {
                    @Override
                    public boolean handleUnknownProperty(DeserializationContext ctxt, JsonParser p,
                                                         JsonDeserializer<?> deserializer, Object beanOrClass,
                                                         String propertyName) throws IOException {
                        log.warn("Unknown property in {}: {}", beanOrClass, propertyName);
                        p.skipChildren();
                        return true;
                    }
                })
                .setDefaultPropertyInclusion(JsonInclude.Value.construct(JsonInclude.Include.CUSTOM,
                        JsonInclude.Include.ALWAYS, PossibleFilter.class, null));
        return new JacksonRedisSerializerFactory(mapper);
    }

    /**
     * Get the default key prefix that is used when creating each store. See {@link #DEFAULT_KEY_PREFIX}.
     *
     * @return the default key prefix
     */
    public static String defaultKeyPrefix() {
        return DEFAULT_KEY_PREFIX;
    }

    @Override
    public boolean hasGenericStores() {
        return true;
    }

    @Override
    public <K extends Comparable<K>, V> Store<K, V> provideGenericStore(Class<K> keyClass, Class<V> valueClass) {
        return new RedisStore<>(connection, valueSerializerFactory.create(valueClass),
                keyPrefix + valueClass.getSimpleName());
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

        private RedisClient redisClient = defaultClient();
        private RedisCodec<String, byte[]> redisCodec = defaultCodec();
        private RedisSerializerFactory valueSerializerFactory = defaultSerializerFactory();
        private String keyPrefix = defaultKeyPrefix();

        public Builder() {
        }

        public Builder redisClient(RedisClient redisClient) {
            this.redisClient = redisClient;
            return this;
        }

        public Builder redisCodec(RedisCodec<String, byte[]> redisCodec) {
            this.redisCodec = redisCodec;
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

        public RedisStoreService build() {
            return new RedisStoreService(redisClient, redisCodec, valueSerializerFactory, keyPrefix);
        }
    }
}
