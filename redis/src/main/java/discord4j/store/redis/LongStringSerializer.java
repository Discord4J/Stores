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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A serializer that converts between {@code Long} and {@code byte[]} through a "string" representation using the
 * specified {@link Charset}.
 */
public class LongStringSerializer implements RedisSerializer<Long> {

    private final Charset charset;

    /**
     * Create a new serializer using the {@link StandardCharsets#UTF_8} charset.
     */
    public LongStringSerializer() {
        this(StandardCharsets.UTF_8);
    }

    /**
     * Create a new serializer using the given {@link Charset}.
     *
     * @param charset charset used to convert objects
     */
    public LongStringSerializer(Charset charset) {
        this.charset = charset;
    }

    @Override
    public byte[] serialize(Long value) throws SerializationException {
        return value.toString().getBytes(charset);
    }

    @Override
    public Long deserialize(byte[] bytes) throws SerializationException {
        return Long.parseUnsignedLong(new String(bytes, charset));
    }
}
