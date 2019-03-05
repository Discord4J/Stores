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

package discord4j.store.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import discord4j.store.api.Store;
import discord4j.store.api.util.WithinRangePredicate;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.io.Serializable;

public class CaffeineStore<K extends Comparable<K>, V extends Serializable> implements Store<K,V> {

    private final Cache<K, V> cache;

    public CaffeineStore(Cache<K, V> cache) {
        this.cache = cache;
    }

    @Override
    public Mono<Void> save(K key, V value) {
        return Mono.fromRunnable(() -> cache.put(key, value));
    }

    @Override
    public Mono<Void> save(Publisher<Tuple2<K, V>> entryStream) {
        return Flux.from(entryStream).doOnNext(t -> save(t.getT1(), t.getT2())).then();
    }

    @Override
    public Mono<V> find(K id) {
        return Mono.fromCallable(() -> cache.get(id, key -> null));
    }

    @Override
    public Flux<V> findInRange(K start, K end) {
        return keys().filter(new WithinRangePredicate<>(start, end)).flatMap(this::find);
    }

    @Override
    public Mono<Long> count() {
        return Mono.fromCallable(cache::estimatedSize);
    }

    @Override
    public Mono<Void> delete(K id) {
        return Mono.fromRunnable(() -> cache.invalidate(id));
    }

    @Override
    public Mono<Void> delete(Publisher<K> ids) {
        return Flux.from(ids).doOnNext(cache::invalidate).then();
    }

    @Override
    public Mono<Void> deleteInRange(K start, K end) {
        return keys().filter(new WithinRangePredicate<>(start, end)).doOnNext(cache::invalidate).then();
    }

    @Override
    public Mono<Void> deleteAll() {
        return Mono.fromRunnable(cache::invalidateAll);
    }

    @Override
    public Flux<K> keys() {
        return Flux.fromIterable(cache.asMap().keySet());
    }

    @Override
    public Flux<V> values() {
        return Flux.fromIterable(cache.asMap().values());
    }

    @Override
    public Mono<Void> invalidate() {
        return Mono.fromRunnable(cache::invalidateAll);
    }
}
