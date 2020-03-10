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
 * A serializer that simply converts between {@code String} and {@code byte[]} using the specified {@link Charset}.
 */
public class StringSerializer implements RedisSerializer<String> {

    private final Charset charset;

    /**
     * Create a new serializer using the {@link StandardCharsets#UTF_8} charset.
     */
    public StringSerializer() {
        this(StandardCharsets.UTF_8);
    }

    /**
     * Create a new serializer using the given {@link Charset}.
     *
     * @param charset charset used to convert objects
     */
    public StringSerializer(Charset charset) {
        this.charset = charset;
    }

    @Override
    public byte[] serialize(String string) throws SerializationException {
        return string.getBytes(charset);
    }

    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        return new String(bytes, charset);
    }
}
