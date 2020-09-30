package discord4j.store.api.wip;

import discord4j.store.api.wip.action.StoreAction;
import reactor.core.publisher.Mono;

public interface Store {

    <R> Mono<R> execute(StoreAction<R> action);
}
