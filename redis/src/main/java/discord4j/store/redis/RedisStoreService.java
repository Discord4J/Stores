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
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import discord4j.store.api.Store;
import discord4j.store.api.primitive.ForwardingStore;
import discord4j.store.api.primitive.LongObjStore;
import discord4j.store.api.service.StoreService;
import discord4j.store.api.util.StoreContext;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.RedisCodec;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

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

    public static final String DEFAULT_REDIS_URI = "redis://localhost";
    public static final String DEFAULT_KEY_PREFIX = "discord4j:store:";

    private final RedisClient client;
    private final String keyPrefix;
    private final StatefulRedisConnection<String, Object> connection;

    /**
     * Creates a new {@link RedisStoreService} with the default client that connects to a localhost server, a default
     * codec performing String key serialization and JSON value serialization using Jackson, and a default key prefix.
     *
     * @see #DEFAULT_REDIS_URI
     * @see #DEFAULT_KEY_PREFIX
     */
    public RedisStoreService() {
        this(defaultClient(), defaultCodec(), defaultKeyPrefix());
    }

    /**
     * Creates a new {@link RedisStoreService} with the given client, and using the default codec performing JSON
     * serialization using Jackson and a default key prefix.
     *
     * @param redisClient the underlying lettuce-core client
     * @see #DEFAULT_KEY_PREFIX
     */
    public RedisStoreService(RedisClient redisClient) {
        this(redisClient, defaultCodec(), defaultKeyPrefix());
    }

    /**
     * Creates a new {@link RedisStoreService} with the given client and codec, and a default key prefix.
     *
     * @param redisClient the underlying lettuce-core client
     * @param redisCodec the codec used to convert between redis and Java
     * @see #DEFAULT_KEY_PREFIX
     */
    public RedisStoreService(RedisClient redisClient, RedisCodec<String, Object> redisCodec) {
        this(redisClient, redisCodec, defaultKeyPrefix());
    }

    /**
     * Creates a new {@link RedisStoreService} with the given client, codec and key prefix.
     *
     * @param redisClient the underlying lettuce-core client
     * @param redisCodec the codec used to convert between redis and Java
     * @param keyPrefix the key prefix used for creating redis keys, appended with the concrete value class simple name.
     */
    public RedisStoreService(RedisClient redisClient, RedisCodec<String, Object> redisCodec, String keyPrefix) {
        this.client = redisClient;
        this.keyPrefix = keyPrefix;
        this.connection = client.connect(redisCodec);
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
    public static RedisCodec<String, Object> defaultCodec() {
        return new StoreRedisCodec<>(new StringSerializer(), new JacksonRedisSerializer(new ObjectMapper()
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .activateDefaultTyping(BasicPolymorphicTypeValidator.builder()
                                .allowIfSubType("discord4j.")
                                .allowIfSubType(Map.class)
                                .allowIfSubType(List.class)
                                .allowIfSubType(CharSequence.class)
                                .allowIfSubType(Number.class)
                                .build(),
                        ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)));
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
    public <K extends Comparable<K>, V> Store<K, V> provideGenericStore(Class<K> keyClass,
                                                                        Class<V> valueClass) {
        return new RedisStore<>(connection, keyPrefix + valueClass.getSimpleName());
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
}
