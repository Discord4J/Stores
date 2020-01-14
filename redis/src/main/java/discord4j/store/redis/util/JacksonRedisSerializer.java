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

package discord4j.store.redis.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import reactor.util.annotation.Nullable;

/**
 * A {@link RedisSerializer} using Jackson to encode/decode objects using default typing. Relies on objects saving their
 * class details given by {@link ObjectMapper#activateDefaultTyping(PolymorphicTypeValidator)}.
 */
public class JacksonRedisSerializer implements RedisSerializer<Object> {

    private final ObjectMapper mapper;

    /**
     * Create a new serializer that uses the given {@link ObjectMapper}. You must be aware that this serializer
     * relies on Jackson's default typing capability to perform generic deserialization, therefore the given {@code
     * ObjectMapper} must be set up for this feature.
     *
     * @param mapper Jackson mapper used to encode/decode objects
     */
    public JacksonRedisSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public byte[] serialize(Object source) throws SerializationException {
        if (source == null) {
            return new byte[0];
        }
        try {
            return mapper.writeValueAsBytes(source);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Could not write JSON: " + e.getMessage(), e);
        }
    }

    @Override
    public Object deserialize(byte[] source) throws SerializationException {
        return deserialize(source, Object.class);
    }

    @Nullable
    public <T> T deserialize(@Nullable byte[] source, Class<T> type) throws SerializationException {
        if (isEmpty(source)) {
            return null;
        }
        try {
            return mapper.readValue(source, type);
        } catch (Exception e) {
            throw new SerializationException("Could not read JSON: " + e.getMessage(), e);
        }
    }

    private static boolean isEmpty(@Nullable byte[] data) {
        return (data == null || data.length == 0);
    }
}
