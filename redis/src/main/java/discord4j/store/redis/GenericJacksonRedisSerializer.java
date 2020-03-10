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

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GenericJacksonRedisSerializer<V> implements RedisSerializer<V> {

    private final ObjectMapper mapper;
    private final Class<V> valueClass;

    public GenericJacksonRedisSerializer(ObjectMapper mapper, Class<V> valueClass) {
        this.mapper = mapper;
        this.valueClass = valueClass;
    }

    @Override
    public byte[] serialize(V v) throws SerializationException {
        try {
            return mapper.writeValueAsBytes(v);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Unable to write JSON: " + v.toString(), e);
        }
    }

    @Override
    public V deserialize(byte[] bytes) throws SerializationException {
        try {
            return mapper.readValue(bytes, valueClass);
        } catch (IOException e) {
            throw new SerializationException("Unable to read JSON: " + new String(bytes, StandardCharsets.UTF_8), e);
        }
    }
}
