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

import discord4j.store.api.primitive.LongObjStore;
import discord4j.store.api.util.LongObjTuple2;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DelegatingLongObjStore<V> implements LongObjStore<V> {

    private final LongObjStore<V> delegate;

    public DelegatingLongObjStore(LongObjStore<V> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Mono<Void> saveWithLong(long key, V value) {
        return delegate.saveWithLong(key, value);
    }

    @Override
    public Mono<Void> saveWithLong(Publisher<LongObjTuple2<V>> entryStream) {
        return delegate.saveWithLong(entryStream);
    }

    @Override
    public Mono<V> find(long id) {
        return delegate.find(id);
    }

    @Override
    public Flux<V> findInRange(long start, long end) {
        return delegate.findInRange(start, end);
    }

    @Override
    public Mono<Void> delete(long id) {
        return delegate.delete(id);
    }

    @Override
    public Mono<Void> delete(Publisher<Long> ids) {
        return delegate.delete(ids);
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
    public Mono<Void> deleteInRange(long start, long end) {
        return delegate.deleteInRange(start, end);
    }

    @Override
    public Mono<Long> count() {
        return delegate.count();
    }

    @Override
    public Flux<Long> keys() {
        return delegate.keys();
    }

    @Override
    public Flux<V> values() {
        return delegate.values();
    }

    @Override
    public String toString() {
        return "DelegatingLongObjStore{" +
                "delegate=" + delegate +
                '}';
    }
}
