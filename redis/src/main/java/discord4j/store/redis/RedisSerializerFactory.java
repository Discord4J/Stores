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

import discord4j.store.api.Store;

/**
 * Factory to create {@link RedisSerializer} instances from a given {@link Store} value type.
 */
public interface RedisSerializerFactory {

    /**
     * Create a {@link RedisSerializer} capable of handling serialization and deserialization for the given type.
     *
     * @param valueClass the type the {@link RedisSerializer} must handle
     * @param <V> value type
     * @return a {@link RedisSerializer} with the given {@code valueClass} type support
     */
    <V> RedisSerializer<V> create(Class<V> valueClass);
}
