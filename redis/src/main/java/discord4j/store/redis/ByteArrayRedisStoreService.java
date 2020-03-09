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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import discord4j.store.api.Store;
import discord4j.store.api.primitive.ForwardingStore;
import discord4j.store.api.primitive.LongObjStore;
import discord4j.store.api.service.StoreService;
import discord4j.store.api.util.StoreContext;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.RedisCodec;
import reactor.core.publisher.Mono;

import java.io.IOException;

public class ByteArrayRedisStoreService implements StoreService {

    public static final String DEFAULT_REDIS_URI = "redis://localhost";
    public static final String DEFAULT_KEY_PREFIX = "discord4j:store:";

    private final RedisClient client;
    private final String keyPrefix;
    private final StatefulRedisConnection<String, byte[]> connection;
    private final ObjectMapper mapper;

    public ByteArrayRedisStoreService(RedisClient redisClient,
                                      RedisCodec<String, byte[]> redisCodec,
                                      ObjectMapper mapper,
                                      String keyPrefix) {
        this.client = redisClient;
        this.mapper = mapper;
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
        return StoreRedisCodec.defaultCodec();
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
        return new ByteArrayRedisStore<>(connection,
                value -> {
                    try {
                        return mapper.writeValueAsBytes(value);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                },
                bytes -> {
                    try {
                        return mapper.readValue(bytes, valueClass);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
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
}
