package discord4j.store.api.wip.action.write.gateway;

abstract class AbstractGatewayWriteAction<R> implements GatewayWriteAction<R> {

    private final int shardIndex;

    AbstractGatewayWriteAction(int shardIndex) {
        this.shardIndex = shardIndex;
    }

    public int getShardIndex() {
        return shardIndex;
    }
}
