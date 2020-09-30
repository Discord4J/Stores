package discord4j.store.api.wip.util;

import discord4j.store.api.wip.action.StoreAction;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class StoreActionHandlingHelper {

    private final Map<Class<? extends StoreAction<?>>, Function<StoreAction<?>, ? extends Mono<?>>> actionHandlers = new HashMap<>();

    private StoreActionHandlingHelper() {
    }

    public static StoreActionHandlingHelper create() {
        return new StoreActionHandlingHelper();
    }

    @SuppressWarnings("unchecked")
    public <R, S extends StoreAction<R>> StoreActionHandlingHelper addHandler(Class<S> actionType,
                                                                              Function<? super S, ? extends Mono<R>> handler) {
        actionHandlers.put(actionType, action -> handler.apply((S) action));
        return this;
    }

    @SuppressWarnings("unchecked")
    public <R> Mono<R> handle(StoreAction<R> action) {
        Function<StoreAction<?>, ? extends Mono<?>> handler = actionHandlers.get(action.getClass());
        if (handler == null) {
            return Mono.error(new NoHandlerFoundException(action));
        }
        return handler.apply(action).map(x -> (R) x);
    }

    public static class NoHandlerFoundException extends RuntimeException {

        private final StoreAction<?> action;

        public NoHandlerFoundException(StoreAction<?> action) {
            super("No handler found for action type " + action.getClass().getName());
            this.action = action;
        }

        public StoreAction<?> getAction() {
            return action;
        }
    }
}
