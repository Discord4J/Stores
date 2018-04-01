package discord4j.store.chronicle;

import discord4j.store.Store;
import discord4j.store.util.WithinRangePredicate;
import net.openhft.chronicle.core.values.LongValue;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public class ChronicleStore<K extends Comparable<K>, V extends Serializable> implements Store<K, V> {

    final ChronicleMap<K, V> map;

    public ChronicleStore(Class<K> keyClass, Class<V> valueClass, boolean persist) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
       this(keyClass, valueClass, persist ? new File("./" + generateName(keyClass, valueClass) + ".mapdata") : null);
    }

    public ChronicleStore(Class<K> keyClass, Class<V> valueClass, @Nullable File location) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        this(makeMap(keyClass, valueClass, location));
    }

    public ChronicleStore(ChronicleMap<K, V> mapInst) {
        this.map = mapInst;
        Runtime.getRuntime().addShutdownHook(new Thread(map::close));
    }

    static <K, V> ChronicleMap<K, V> makeMap(Class<K> keyClass, Class<V> valueClass, @Nullable File persistLoc) throws
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        ChronicleMapBuilder<K, V> builder =  ChronicleMapBuilder.of(keyClass, valueClass)
                .name(generateName(keyClass, valueClass))
                .averageKey(keyClass.equals(LongValue.class) ? (K) (Long) 0L : keyClass.getDeclaredConstructor().newInstance())
                .averageValue(valueClass.getDeclaredConstructor().newInstance())
                .entries(200_000)
                .maxBloatFactor(5.0);
        return persistLoc != null ? builder.createPersistedTo(persistLoc) : builder.create();
    }

    static String generateName(Class<?> k, Class<?> v) {
        return String.format("D4J-Store-%s-%s", k.getSimpleName(), v.getSimpleName());
    }

    @Override
    public Mono<Void> save(K key, V value) {
        return Mono.fromRunnable(() -> map.put(key, value));
    }

    @Override
    public Mono<Void> save(Iterable<Tuple2<K, V>> entries) {
        return Flux.fromIterable(entries).flatMap(tuple -> save(tuple.getT1(), tuple.getT2())).then();
    }

    @Override
    public Mono<Void> save(Publisher<Tuple2<K, V>> entryStream) {
        return Flux.from(entryStream).flatMap(tuple -> save(tuple.getT1(), tuple.getT2())).then();
    }

    @Override
    public Mono<V> find(K id) {
        return Mono.just(id).map(map::get);
    }

    @Override
    public Mono<Boolean> exists(K id) {
        return Mono.just(id).map(map::containsKey);
    }

    @Override
    public Mono<Boolean> exists(Publisher<K> ids) {
        return Flux.from(ids).all(map::containsKey);
    }

    @Override
    public Flux<V> findAll(Iterable<K> ids) {
        return Flux.defer(() -> Flux.fromIterable(ids)).map(map::get);
    }

    @Override
    public Flux<V> findAll(Publisher<K> ids) {
        return Flux.from(ids).map(map::get);
    }

    @Override
    public Flux<V> findInRange(K start, K end) {
        return keys().filter(new WithinRangePredicate<>(start, end)).map(map::get);
    }

    @Override
    public Mono<Long> count() {
        return Mono.just(map.longSize());
    }

    @Override
    public Mono<Void> delete(K id) {
        return Mono.just(id).map(map::remove).then();
    }

    @Override
    public Mono<Void> delete(Publisher<K> ids) {
        return Flux.from(ids).doOnNext(map::remove).then();
    }

    @Override
    public Mono<Void> delete(Tuple2<K, V> entry) {
        return Mono.defer(() -> {
            map.remove(entry.getT1(), entry.getT2());
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> deleteInRange(K start, K end) {
        return keys().filter(new WithinRangePredicate<>(start, end)).doOnNext(map::remove).then();
    }

    @Override
    public Mono<Void> deleteAll(Iterable<Tuple2<K, V>> entries) {
        return Flux.fromIterable(entries).flatMap(this::delete).then();
    }

    @Override
    public Mono<Void> deleteAll(Publisher<Tuple2<K, V>> entries) {
        return Flux.from(entries).flatMap(this::delete).then();
    }

    @Override
    public Mono<Void> deleteAll() {
        return Mono.fromRunnable(map::clear);
    }

    @Override
    public Flux<K> keys() {
        return Flux.fromIterable(map.keySet());
    }

    @Override
    public Flux<V> values() {
        return Flux.fromIterable(map.values());
    }
}
