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
package discord4j.store.api;

import discord4j.store.api.primitive.LongObjStore;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.io.Serializable;

/**
 * A store that only supports read operations.
 *
 * @param <K> The key type which provides a 1:1 mapping to the value type. This type is also expected to be
 * {@link Comparable} in order to allow for range operations.
 * @param <V> The value type, these follow
 * <a href="https://en.wikipedia.org/wiki/JavaBeans#JavaBean_conventions">JavaBean</a> conventions.
 * @see LongObjStore
 */
public interface ReadOnlyStore<K extends Comparable<K>, V extends Serializable> {

    /**
     * Attempts to find the value associated with the provided id.
     *
     * @param id The id to search with.
     * @return A mono, which may or may not contain an associated object.
     */
    Mono<V> find(K id);

    /**
     * Retrieves all stored values with ids within a provided range.
     *
     * @param start The starting key (inclusive).
     * @param end The ending key (exclusive).
     * @return The stream of values with ids within the provided range.
     */
    Flux<V> findInRange(K start, K end);

    /**
     * Retrieves the amount of stored values in the data source currently.
     *
     * @return A mono which provides the amount of stored values.
     */
    Mono<Long> count();

    /**
     * Gets a stream of all keys in the data source.
     *
     * @return The stream of keys stored.
     */
    Flux<K> keys();

    /**
     * Gets a stream of all values in the data source.
     *
     * @return The stream of values stored.
     */
    Flux<V> values();

    /**
     * Gets a stream of all entries in the data source.
     *
     * @return The stream of all entries stored.
     */
    default Flux<Tuple2<K, V>> entries() {
        return keys().zipWith(values());
    }
}
