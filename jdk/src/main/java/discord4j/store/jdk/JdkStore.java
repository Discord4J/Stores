package discord4j.store.jdk;

import discord4j.store.api.Store;
import discord4j.store.api.util.WithinRangePredicate;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

public class JdkStore<K extends Comparable<K>, V extends Serializable> implements Store<K, V> {

    private final Map<K, V> map;

    public JdkStore(Map<K, V> map) {
        this.map = map;
    }

    public JdkStore(boolean persist) {
        if (persist) {
            this.map = new ConcurrentHashMap<>();
        } else {
            this.map = Collections.synchronizedMap(new WeakHashMap<>());
        }
    }

    @Override
    public Mono<Void> save(K key, V value) {
        return Mono.fromRunnable(() -> map.put(key, value));
    }

    @Override
    public Mono<Void> save(Publisher<Tuple2<K, V>> entryStream) {
        return Flux.from(entryStream).doOnNext(t -> map.put(t.getT1(), t.getT2())).then();
    }

    @Override
    public Mono<V> find(K id) {
        return Mono.defer(() -> {
            if (map.containsKey(id))
                return Mono.just(map.get(id));
            return Mono.empty();
        });
    }

    @Override
    public Flux<V> findInRange(K start, K end) {
        return keys().filter(new WithinRangePredicate<>(start, end)).map(map::get);
    }

    @Override
    public Mono<Long> count() {
        return Mono.fromSupplier(() -> (long) map.size());
    }

    @Override
    public Mono<Void> delete(K id) {
        return Mono.just(id).doOnNext(map::remove).then();
    }

    @Override
    public Mono<Void> delete(Publisher<K> ids) {
        return Flux.from(ids).doOnNext(map::remove).then();
    }

    @Override
    public Mono<Void> deleteInRange(K start, K end) {
        return keys().filter(new WithinRangePredicate<>(start, end)).doOnNext(map::remove).then();
    }

    @Override
    public Mono<Void> deleteAll() {
        return Mono.fromRunnable(map::clear);
    }

    @Override
    public Flux<K> keys() {
        return Flux.defer(() -> Flux.fromIterable(map.keySet()));
    }

    @Override
    public Flux<V> values() {
        return Flux.defer(() -> Flux.fromIterable(map.values()));
    }

    @Override
    public Flux<Tuple2<K, V>> entries() {
        return Flux.defer(() -> Flux.fromIterable(map.entrySet())).map(e -> Tuples.of(e.getKey(), e.getValue()));
    }

    @Override
    public Mono<Void> invalidate() {
        return Mono.fromRunnable(map::clear);
    }

    @Override
    public String toString() {
        return "JdkStore{" +
            "map=" + map +
            '}';
    }
}
