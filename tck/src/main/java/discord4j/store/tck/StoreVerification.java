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

package discord4j.store.tck;

import discord4j.store.Store;
import discord4j.store.primitive.LongObjStore;
import discord4j.store.service.StoreService;
import discord4j.store.util.LongObjTuple2;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class StoreVerification {

    public abstract StoreService getStoreService();

    @Test
    public void testKeyValueSave() {
        SampleBean bean = new SampleBean();
        if (getStoreService().hasGenericStores()) {
            Store<String, SampleBean> store = getStoreService().provideGenericStore(String.class, SampleBean.class);
            String key = String.valueOf(bean.getId());
            Mono<SampleBean> saveScenario = store.save(key, bean)
                    .then(Mono.defer(() -> store.find(key)));
            StepVerifier.create(saveScenario)
                    .expectNextMatches(next -> next.equals(bean))
                    .verifyComplete();
        }
        if (getStoreService().hasLongObjStores()) {
            LongObjStore<SampleBean> store = getStoreService().provideLongObjStore(SampleBean.class);
            long key = bean.getId();
            Mono<SampleBean> saveScenario = store.saveWithLong(key, bean)
                    .then(Mono.defer(() -> store.find(key)));
            StepVerifier.create(saveScenario)
                    .expectNextMatches(next -> next.equals(bean))
                    .verifyComplete();
        }
    }
}
