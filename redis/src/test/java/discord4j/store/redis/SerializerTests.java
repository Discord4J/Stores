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

import discord4j.discordjson.Id;
import discord4j.store.api.util.LongLongTuple2;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class SerializerTests {

    @Test
    public void testByteArraySerializers() {
        LongLongTuple2 k1 = LongLongTuple2.of(210938552563925002L, 134127815531560960L);
        Long k2 = 129498893795721216L;
        String k3 = "discord4j";
        LongLongTuple2Serializer s1 = new LongLongTuple2Serializer();
        LongSerializer s2 = new LongSerializer();
        StringSerializer s3 = new StringSerializer();
        assertEquals(k1, s1.deserialize(s1.serialize(k1)));
        assertEquals(k2, s2.deserialize(s2.serialize(k2)));
        assertEquals(k3, s3.deserialize(s3.serialize(k3)));
    }

    @Test
    public void testStringSerializers() {
        LongLongTuple2 k1 = LongLongTuple2.of(210938552563925002L, 134127815531560960L);
        Long k2 = 129498893795721216L;
        String k3 = "discord4j";
        LongLongTuple2StringSerializer s1 = new LongLongTuple2StringSerializer();
        LongStringSerializer s2 = new LongStringSerializer();
        StringSerializer s3 = new StringSerializer();
        assertEquals(k1, s1.deserialize(s1.serialize(k1)));
        assertEquals(k2, s2.deserialize(s2.serialize(k2)));
        assertEquals(k3, s3.deserialize(s3.serialize(k3)));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void testRawTypeSerializer() {
        RedisSerializer<Set> rawSerializer = RedisStoreDefaults.jacksonValueSerializerFactory().create(Set.class);
        Set rawSet = new HashSet();
        rawSet.add(Id.of("123456789012345678").asLong());
        byte[] written = rawSerializer.serialize(rawSet);
        Set read = rawSerializer.deserialize(written);
        Assertions.assertThat(rawSet).hasSameElementsAs(read);
    }
}
