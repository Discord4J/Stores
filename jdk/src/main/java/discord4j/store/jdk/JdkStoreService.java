package discord4j.store.jdk;

import com.google.auto.service.AutoService;
import discord4j.store.Store;
import discord4j.store.primitive.ForwardingStore;
import discord4j.store.primitive.LongObjStore;
import discord4j.store.service.StoreService;
import discord4j.store.util.StoreContext;
import reactor.core.publisher.Mono;

import java.io.Serializable;

@AutoService(StoreService.class)
public class JdkStoreService implements StoreService {

    volatile Class<?> messageClass;

    @Override
    public boolean hasGenericStores() {
        return true;
    }

    @Override
    public <K extends Comparable<K>, V extends Serializable> Store<K, V> provideGenericStore(Class<K> keyClass,
                                                                                             Class<V> valueClass) {
        return new JdkStore<>(!valueClass.equals(messageClass));
    }

    @Override
    public boolean hasLongObjStores() {
        return false;
    }

    @Override
    public <V extends Serializable> LongObjStore<V> provideLongObjStore(Class<V> valueClass) {
        return new ForwardingStore<>(provideGenericStore(Long.class, valueClass));
    }

    @Override
    public Mono<Void> init(StoreContext context) {
        messageClass = context.getMessageClass();
        return Mono.empty();
    }

    @Override
    public Mono<Void> dispose() {
        return Mono.empty();
    }
}
