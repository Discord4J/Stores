package discord4j.store.api.wip.action.write.gateway;

public class InvalidateShardAction extends AbstractGatewayWriteAction<Void> {

    public enum Cause {
        HARD_RECONNECT, LOGOUT;
    }

    private final Cause cause;

    public InvalidateShardAction(int shardIndex, Cause cause) {
        super(shardIndex);
        this.cause = cause;
    }

    public Cause getCause() {
        return cause;
    }
}
