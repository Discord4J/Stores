package discord4j.store.chronicle;

import com.google.auto.service.AutoService;
import discord4j.store.Store;
import discord4j.store.primitive.LongObjStore;
import discord4j.store.service.StoreService;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

@AutoService(StoreService.class)
public class ChronicleStoreService implements StoreService {

    boolean shouldPersist = Boolean.parseBoolean(System.getenv("Persist-Store"));

    @Override
    public boolean hasGenericStores() {
        return true;
    }

    @Override
    public <K extends Comparable<K>, V extends Serializable> Store<K, V> provideGenericStore(Class<K> keyClass,
                                                                                             Class<V> valueClass) {
        try {
            return new ChronicleStore<>(keyClass, valueClass, shouldPersist);
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
            return new LongChronicleStore<>(valueClass, shouldPersist);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
