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

package discord4j.store.api.mapping;

import discord4j.store.api.Store;
import discord4j.store.api.noop.NoOpStoreService;
import discord4j.store.api.primitive.LongObjStore;
import discord4j.store.api.service.StoreService;
import discord4j.store.api.util.StoreContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.Nullable;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MappingStoreService implements StoreService {

    private final Map<Class<?>, StoreService> serviceMap;
    private final StoreService fallback;

    private MappingStoreService(Map<Class<?>, StoreService> serviceMap, @Nullable StoreService fallback) {
        this.serviceMap = serviceMap;
        this.fallback = fallback == null ? new NoOpStoreService() : fallback;
    }

    public static MappingStoreService create() {
        return new MappingStoreService(new ConcurrentHashMap<>(), null);
    }

    public MappingStoreService addService(Class<?> valueClass, StoreService storeService) {
        Map<Class<?>, StoreService> updatedMap = new ConcurrentHashMap<>(serviceMap);
        updatedMap.put(valueClass, storeService);
        return new MappingStoreService(updatedMap, fallback);
    }

    public MappingStoreService setFallback(@Nullable StoreService storeService) {
        return new MappingStoreService(serviceMap, storeService);
    }

    @Override
    public boolean hasGenericStores() {
        return serviceMap.values().stream().anyMatch(StoreService::hasGenericStores) || fallback.hasGenericStores();
    }

    @Override
    public <K extends Comparable<K>, V extends Serializable> Store<K, V> provideGenericStore(Class<K> keyClass,
                                                                                             Class<V> valueClass) {
        return getGenericStore(valueClass).provideGenericStore(keyClass, valueClass);
    }

    private <V> StoreService getGenericStore(Class<V> valueClass) {
        StoreService service = serviceMap.getOrDefault(valueClass, fallback);
        return service.hasGenericStores() ? service : fallback;
    }

    @Override
    public boolean hasLongObjStores() {
        return serviceMap.values().stream().anyMatch(StoreService::hasLongObjStores) || fallback.hasLongObjStores();
    }

    @Override
    public <V extends Serializable> LongObjStore<V> provideLongObjStore(Class<V> valueClass) {
        return getLongObjStore(valueClass).provideLongObjStore(valueClass);
    }

    private <V> StoreService getLongObjStore(Class<V> valueClass) {
        StoreService service = serviceMap.getOrDefault(valueClass, fallback);
        return service.hasLongObjStores() ? service : fallback;
    }

    @Override
    public Mono<Void> init(StoreContext context) {
        return Flux.fromIterable(serviceMap.values())
                .concatWithValues(fallback)
                .flatMap(service -> service.init(context))
                .then();
    }

    @Override
    public Mono<Void> dispose() {
        return Flux.fromIterable(serviceMap.values())
                .concatWithValues(fallback)
                .flatMap(StoreService::dispose)
                .then();
    }
}
