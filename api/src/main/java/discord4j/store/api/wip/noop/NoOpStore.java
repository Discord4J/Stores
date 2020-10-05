package discord4j.store.api.wip.noop;

import discord4j.store.api.wip.Store;
import discord4j.store.api.wip.StoreAction;
import reactor.core.publisher.Mono;

/**
 * Implementation of {@link Store} that does nothing.
 */
public class NoOpStore implements Store {

    private NoOpStore() {
    }

    public static NoOpStore create() {
        return new NoOpStore();
    }

    @Override
    public <R> Mono<R> execute(StoreAction<R> action) {
        return Mono.empty();
    }
}
