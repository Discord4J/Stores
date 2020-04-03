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

package discord4j.store.api.delegating;

import discord4j.store.api.Store;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public class DelegatingStore<K extends Comparable<K>, V> implements Store<K, V> {
    
    private final Store<K, V> delegate;

    public DelegatingStore(Store<K, V> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Mono<Void> save(K key, V value) {
        return delegate.save(key, value);
    }

    @Override
    public Mono<Void> save(Publisher<Tuple2<K, V>> entryStream) {
        return delegate.save(entryStream);
    }

    @Override
    public Mono<Void> delete(K id) {
        return delegate.delete(id);
    }

    @Override
    public Mono<Void> delete(Publisher<K> ids) {
        return delegate.delete(ids);
    }

    @Override
    public Mono<Void> deleteInRange(K start, K end) {
        return delegate.deleteInRange(start, end);
    }

    @Override
    public Mono<Void> deleteAll() {
        return delegate.deleteAll();
    }

    @Override
    public Mono<Void> invalidate() {
        return delegate.invalidate();
    }

    @Override
    public Mono<V> find(K id) {
        return delegate.find(id);
    }

    @Override
    public Flux<V> findInRange(K start, K end) {
        return delegate.findInRange(start, end);
    }

    @Override
    public Mono<Long> count() {
        return delegate.count();
    }

    @Override
    public Flux<K> keys() {
        return delegate.keys();
    }

    @Override
    public Flux<V> values() {
        return delegate.values();
    }
}
