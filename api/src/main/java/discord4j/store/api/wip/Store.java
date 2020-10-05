package discord4j.store.api.wip;

import reactor.core.publisher.Mono;

public interface Store {

    <R> Mono<R> execute(StoreAction<R> action);
}
