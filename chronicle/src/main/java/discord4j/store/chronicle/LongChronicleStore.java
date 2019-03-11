package discord4j.store.chronicle;

import discord4j.store.api.primitive.LongObjStore;
import discord4j.store.api.util.LongObjTuple2;
import discord4j.store.api.util.WithinRangePredicate;
import net.openhft.chronicle.core.values.LongValue;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.values.Values;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public class LongChronicleStore<V extends Serializable> implements LongObjStore<V> {

    final ChronicleMap<LongValue, V> map;

    public LongChronicleStore(Class<V> valueClass, boolean persist) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        this(valueClass, persist ? new File("./" + ChronicleStore.generateName(LongValue.class, valueClass) +
                ".mapdata") : null);
    }

    public LongChronicleStore(Class<V> valueClass, @Nullable File location) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        this(ChronicleStore.makeMap(LongValue.class, valueClass, location));
    }

    public LongChronicleStore(ChronicleMap<LongValue, V> mapInst) {
        this.map = mapInst;
    }

    static LongValue toValue(long l) {
        LongValue val = Values.newHeapInstance(LongValue.class);
        val.setValue(l);
        return val;
    }

    @Override
    public Mono<Void> saveWithLong(long key, V value) {
        return Mono.defer(() -> {
            map.put(toValue(key), value);
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> saveWithLong(Publisher<LongObjTuple2<V>> entryStream) {
        return Flux.from(entryStream).map(entry -> saveWithLong(entry.getT1(), entry.getT2())).then();
    }

    @Override
    public Mono<V> find(long id) {
        return Mono.just(toValue(id)).map(map::get);
    }

    @Override
    public Flux<V> findInRange(long start, long end) {
        return keys().filter(new WithinRangePredicate<>(start, end)).map(LongChronicleStore::toValue).map(map::get);
    }

    @Override
    public Mono<Long> count() {
        return Mono.just(map.longSize());
    }

    @Override
    public Mono<Void> delete(long id) {
        return Mono.just(id).map(LongChronicleStore::toValue).doOnNext(map::remove).then();
    }

    @Override
    public Mono<Void> delete(Publisher<Long> ids) {
        return Flux.from(ids).map(LongChronicleStore::toValue).doOnNext(map::remove).then();
    }

    @Override
    public Mono<Void> deleteAll() {
        return Mono.fromRunnable(map::clear);
    }

    @Override
    public Flux<Long> keys() {
        return Flux.fromIterable(map.keySet()).map(LongValue::getValue);
    }

    @Override
    public Flux<V> values() {
        return Flux.fromIterable(map.values());
    }

    @Override
    public Mono<Void> deleteInRange(long start, long end) {
        return keys().filter(new WithinRangePredicate<>(start, end)).map(LongChronicleStore::toValue).doOnNext(map::remove).then();
    }

    @Override
    public Mono<Void> invalidate() {
        return Mono.fromRunnable(map::clear);
    }

    public void close() {
        map.close();
    }
}
