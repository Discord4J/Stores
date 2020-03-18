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

import discord4j.store.api.util.LongLongTuple2;

/**
 * A serializer that converts between {@code LongLongTuple2} and {@code byte[]}.
 */
public class LongLongTuple2Serializer implements RedisSerializer<LongLongTuple2> {

    /**
     * Create a new serializer.
     */
    public LongLongTuple2Serializer() {
    }

    @Override
    public byte[] serialize(LongLongTuple2 t2) throws SerializationException {
        byte[] result = new byte[16];
        long l = t2.getT1();
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (l & 0xFF);
            l >>= 8;
        }
        l = t2.getT2();
        for (int i = 15; i >= 8; i--) {
            result[i] = (byte) (l & 0xFF);
            l >>= 8;
        }
        return result;
    }

    @Override
    public LongLongTuple2 deserialize(byte[] bytes) throws SerializationException {
        long result1 = 0;
        for (int i = 0; i < Long.BYTES; i++) {
            result1 <<= Long.BYTES;
            result1 |= (bytes[i] & 0xFF);
        }
        long result2 = 0;
        for (int i = 8; i < Long.BYTES + 8; i++) {
            result2 <<= Long.BYTES;
            result2 |= (bytes[i] & 0xFF);
        }
        return LongLongTuple2.of(result1, result2);
    }
}
