package discord4j.store.jdk;

import discord4j.store.api.Store;
import discord4j.store.api.util.WithinRangePredicate;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link Store} backed by a JDK {@link Map}.
 *
 * @param <K> the store keys
 * @param <V> the store values
 */
public class JdkStore<K extends Comparable<K>, V> implements Store<K, V> {

    private final Map<K, V> map;

    /**
     * Create a new {@link JdkStore} backed by the given {@link Map}. Operations on the map must be thread-safe.
     *
     * @param map the map to back this store
     */
    public JdkStore(Map<K, V> map) {
        this.map = map;
    }

    /**
     * Create a new {@link JdkStore} using a default backing {@link Map}, like {@link ConcurrentHashMap}.
     *
     * @param persist whether this store is expected to hold onto the stored values indefinitely. Will use a
     * {@link ConcurrentHashMap} as backing if {@code true}, and a thread-safe {@link WeakHashMap} otherwise.
     */
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
            if (map.containsKey(id)) {
                return Mono.just(map.get(id));
            }
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

    protected Map<K, V> getMap() {
        return map;
    }

    @Override
    public String toString() {
        return "JdkStore@" + Integer.toHexString(hashCode()) + "{" +
                "map=" + map.getClass().getCanonicalName() +
                '}';
    }
}
