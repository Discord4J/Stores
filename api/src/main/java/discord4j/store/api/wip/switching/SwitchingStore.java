package discord4j.store.api.wip.switching;

import discord4j.store.api.wip.Store;
import discord4j.store.api.wip.action.StoreAction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Predicate;

public class SwitchingStore implements Store {

    private final List<ConditionStore> conditionStores;

    private SwitchingStore(List<ConditionStore> conditionStores) {
        this.conditionStores = Collections.unmodifiableList(conditionStores);
    }

    @Override
    public <R> Mono<R> execute(StoreAction<R> action) {
        return Flux.fromIterable(conditionStores)
                .filter(conditionStore -> conditionStore.condition.test(action))
                .flatMap(conditionStore -> conditionStore.store.execute(action))
                .takeLast(1)
                .next();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final List<ConditionStore> conditionStores = new ArrayList<>();

        public final Builder useIfActionMatches(Store store, Predicate<StoreAction<?>> condition) {
            Objects.requireNonNull(store);
            Objects.requireNonNull(condition);
            conditionStores.add(new ConditionStore(store, condition));
            return this;
        }

        @SafeVarargs
        public final Builder useIfActionOfType(Store store, Class<? extends StoreAction<?>> actionType,
                                         Class<? extends StoreAction<?>>... moreTypes) {
            Objects.requireNonNull(store);
            Objects.requireNonNull(actionType);
            Objects.requireNonNull(moreTypes);
            conditionStores.add(new ConditionStore(store, action -> actionType.isInstance(action)
                || Arrays.stream(moreTypes).anyMatch(type -> type.isInstance(action))));
            return this;
        }

        public final Builder useForAllActions(Store store) {
            Objects.requireNonNull(store);
            conditionStores.add(new ConditionStore(store, action -> true));
            return this;
        }

        public final SwitchingStore build() {
            return new SwitchingStore(conditionStores);
        }
    }

    private static class ConditionStore {

        private final Store store;
        private final Predicate<StoreAction<?>> condition;

        ConditionStore(Store store, Predicate<StoreAction<?>> condition) {
            this.store = store;
            this.condition = condition;
        }

        @Override
        public String toString() {
            return "ConditionStore{" +
                "store=" + store +
                ", condition=" + condition +
                '}';
        }
    }
}
