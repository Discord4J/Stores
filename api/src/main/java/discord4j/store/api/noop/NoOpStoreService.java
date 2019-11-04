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
package discord4j.store.api.noop;

import discord4j.store.api.Store;
import discord4j.store.api.noop.primitive.NoOpLongObjStore;
import discord4j.store.api.primitive.LongObjStore;
import discord4j.store.api.service.StoreService;
import discord4j.store.api.util.StoreContext;
import reactor.core.publisher.Mono;

/**
 * Service which provides stores that do nothing. This is automatically used if no valid save services are found.
 *
 * @see NoOpStore
 * @see NoOpLongObjStore
 */
public class NoOpStoreService implements StoreService {

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean hasGenericStores() {
        return true;
    }

    @Override
    public <K extends Comparable<K>, V> Store<K, V> provideGenericStore(Class<K> keyClass,
                                                                        Class<V> valueClass) {
        return new NoOpStore<>();
    }

    @Override
    public boolean hasLongObjStores() {
        return true;
    }

    @Override
    public <V> LongObjStore<V> provideLongObjStore(Class<V> valueClass) {
        return new NoOpLongObjStore<>();
    }

    @Override
    public void init(StoreContext context) {
    }

    @Override
    public Mono<Void> dispose() {
        return Mono.empty();
    }
}
