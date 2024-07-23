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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import discord4j.discordjson.possible.PossibleFilter;
import discord4j.discordjson.possible.PossibleModule;
import discord4j.store.api.util.LongLongTuple2;
import io.lettuce.core.RedisClient;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.io.IOException;

public class RedisStoreDefaults {

    private static final Logger log = Loggers.getLogger(RedisStoreDefaults.class);

    public static final String DEFAULT_REDIS_URI = "redis://localhost";
    public static final String DEFAULT_KEY_PREFIX = "discord4j:store:";

    private RedisStoreDefaults() {}

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
     * Get the default cluster client, connecting to localhost, or to a custom server given by the {@code D4J_REDIS_URL}
     * environment variable.
     *
     * @return the default lettuce-core client
     */
    public static RedisClusterClient defaultClusterClient() {
        String url = System.getenv("D4J_REDIS_URL");
        String redisUri = url != null ? url : DEFAULT_REDIS_URI;
        return RedisClusterClient.create(redisUri);
    }

    /**
     * Get the default key prefix that is used when creating each store. See {@link #DEFAULT_KEY_PREFIX}.
     *
     * @return the default key prefix
     */
    public static String keyPrefix() {
        return DEFAULT_KEY_PREFIX;
    }

    /**
     * Get the default capable of encoding keys and values sent to Redis, and decodes keys and values in the command
     * output.
     *
     * @return the default redis codec
     */
    public static RedisCodec<byte[], byte[]> byteArrayCodec() {
        return new ByteArrayCodec();
    }

    /**
     * Get the default factory capable of serializing and deserialize {@link LongLongTuple2}, {@link Long} or
     * {@link String} keys in a byte array form.
     *
     * @return a default {@link RedisSerializerFactory} for keys
     */
    public static RedisSerializerFactory byteArrayKeySerializerFactory() {
        return new RedisSerializerFactory() {
            @SuppressWarnings("unchecked")
            @Override
            public <V> RedisSerializer<V> create(Class<V> type) {
                if (type == LongLongTuple2.class) {
                    return (RedisSerializer<V>) new LongLongTuple2Serializer();
                } else if (type == String.class) {
                    return (RedisSerializer<V>) new StringSerializer();
                } else if (type == Long.class) {
                    return (RedisSerializer<V>) new LongSerializer();
                } else {
                    throw new IllegalArgumentException("Unsupported type: " + type);
                }
            }
        };
    }

    /**
     * Get the default factory capable of serializing and deserialize {@link LongLongTuple2}, {@link Long} or
     * {@link String} keys in a String or "human readable" form.
     *
     * @return a default {@link RedisSerializerFactory} for keys
     */
    public static RedisSerializerFactory stringKeySerializerFactory() {
        return new RedisSerializerFactory() {
            @SuppressWarnings("unchecked")
            @Override
            public <V> RedisSerializer<V> create(Class<V> type) {
                if (type == LongLongTuple2.class) {
                    return (RedisSerializer<V>) new LongLongTuple2StringSerializer();
                } else if (type == String.class) {
                    return (RedisSerializer<V>) new StringSerializer();
                } else if (type == Long.class) {
                    return (RedisSerializer<V>) new LongStringSerializer();
                } else {
                    throw new IllegalArgumentException("Unsupported type: " + type);
                }
            }
        };
    }

    /**
     * Get the default factory capable of using Jackson to serialize and deserialize values.
     *
     * @return a default {@link RedisSerializerFactory} for values
     */
    public static RedisSerializerFactory jacksonValueSerializerFactory() {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new PossibleModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
                .setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.PUBLIC_ONLY)
                .setVisibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.ANY)
                .setDefaultPropertyInclusion(JsonInclude.Value.construct(JsonInclude.Include.CUSTOM,
                        JsonInclude.Include.ALWAYS, PossibleFilter.class, null))
                .addHandler(new DeserializationProblemHandler() {
                    @Override
                    public boolean handleUnknownProperty(DeserializationContext ctxt, JsonParser p,
                                                         JsonDeserializer<?> deserializer, Object beanOrClass,
                                                         String propertyName) throws IOException {
                        log.warn("Unknown property in {}: {}", beanOrClass, propertyName);
                        p.skipChildren();
                        return true;
                    }
                });
        return new JacksonRedisSerializerFactory(mapper);
    }
}
