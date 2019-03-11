package discord4j.store.jdk;

import com.austinv11.servicer.WireService;
import discord4j.store.api.Store;
import discord4j.store.api.primitive.ForwardingStore;
import discord4j.store.api.primitive.LongObjStore;
import discord4j.store.api.service.StoreService;
import discord4j.store.api.util.StoreContext;
import reactor.core.publisher.Mono;

import java.io.Serializable;

@WireService(StoreService.class)
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
    public void init(StoreContext context) {
        messageClass = context.getMessageClass();
    }

    @Override
    public Mono<Void> dispose() {
        return Mono.empty();
    }
}
