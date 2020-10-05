package discord4j.store.api.wip.handler;

import discord4j.store.api.wip.StoreAction;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class ActionHandler {

    private final ActionMapper actionMapper;

    private ActionHandler(ActionMapper actionMapper) {
        this.actionMapper = actionMapper;
    }

    /**
     * Creates a {@link ActionHandler} that will use the given mapper to handle actions.
     *
     * @param actionMapper the mapper
     * @return a new {@link ActionHandler}
     */
    public static ActionHandler withMapper(ActionMapper actionMapper) {
        Objects.requireNonNull(actionMapper);
        return new ActionHandler(actionMapper);
    }

    /**
     * Creates a {@link ActionHandler} that will use the given mappers to handle actions. All mappers are aggregated
     * and the result will be used to map actions. Each mapper must handle a distinct set of actions, any overlap
     * will result in an {@link IllegalStateException}.
     *
     * @param actionMapper the mapper
     * @param more         additional mappers to be integrated
     * @return a new {@link ActionHandler}
     * @throws IllegalStateException if more than one mapper defines a mapping for the same action type
     */
    public static ActionHandler withMappers(ActionMapper actionMapper, ActionMapper... more) {
        Objects.requireNonNull(more);
        if (more.length == 0) {
            return withMapper(actionMapper);
        }
        Objects.requireNonNull(actionMapper);
        List<ActionMapper> mappers = new ArrayList<>();
        mappers.add(actionMapper);
        mappers.addAll(Arrays.asList(more));
        return new ActionHandler(ActionMapper.aggregate(mappers));
    }

    /**
     * Handles the action using the provided mappers, calling the proper method according to the action type. If no
     * mapping exists for the given action, this method is a no-op.
     *
     * @param action the action to handle
     * @param <R>    the return type of the action
     * @return the result of the handling of the action
     */
    @SuppressWarnings("unchecked")
    public <R> Mono<R> handle(StoreAction<R> action) {
        Function<StoreAction<?>, ? extends Mono<?>> handler = actionMapper.get(action.getClass());
        return Mono.justOrEmpty(handler).map(h -> (R) h.apply(action));
    }
}
