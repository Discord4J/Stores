package discord4j.store.api.wip.action.write.gateway;

public class InvalidateShardAction extends AbstractGatewayWriteAction<Void> {

    public InvalidateShardAction(int shardIndex) {
        super(shardIndex);
    }
}
