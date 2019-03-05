package discord4j.store.chronicle;

import com.austinv11.servicer.WireService;
import discord4j.store.api.Store;
import discord4j.store.api.primitive.LongObjStore;
import discord4j.store.api.service.StoreService;
import discord4j.store.api.util.StoreContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@WireService(StoreService.class)
public class ChronicleStoreService implements StoreService {

    final List<ChronicleStore<?, ?>> storeTracker = new CopyOnWriteArrayList<>();
    final List<LongChronicleStore<?>> longStoreTracker = new CopyOnWriteArrayList<>();

    boolean shouldPersist;
    volatile Class<?> messageClass;

    public ChronicleStoreService() {
        this(Boolean.parseBoolean(System.getenv("Persist-Store")));
    }

    public ChronicleStoreService(boolean shouldPersist) {
        this.shouldPersist = shouldPersist;
    }

    @Override
    public boolean hasGenericStores() {
        return true;
    }

    @Override
    public <K extends Comparable<K>, V extends Serializable> Store<K, V> provideGenericStore(Class<K> keyClass,
                                                                                             Class<V> valueClass) {
        try {
            ChronicleStore<K, V> store = new ChronicleStore<>(keyClass, valueClass, shouldPersist && !valueClass.equals(messageClass));
            storeTracker.add(store);
            return store;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | IOException e) {
           throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasLongObjStores() {
        return true;
    }

    @Override
    public <V extends Serializable> LongObjStore<V> provideLongObjStore(Class<V> valueClass) {
        try {
            LongChronicleStore<V> store = new LongChronicleStore<>(valueClass, shouldPersist && !valueClass.equals(messageClass));
            longStoreTracker.add(store);
            return store;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Mono<Void> init(StoreContext context) {
        messageClass = context.getMessageClass();
        return Mono.empty();
    }

    @Override
    public Mono<Void> dispose() {
        return Flux.fromIterable(storeTracker)
                .doOnNext(ChronicleStore::close)
                .thenMany(Flux.fromIterable(longStoreTracker))
                .doOnNext(LongChronicleStore::close)
                .then();
    }
}
