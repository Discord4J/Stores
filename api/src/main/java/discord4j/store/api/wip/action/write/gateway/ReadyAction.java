package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.gateway.Ready;

public class ReadyAction extends AbstractGatewayWriteAction<Void> {

    private final Ready ready;

    public ReadyAction(int shardIndex, Ready ready) {
        super(shardIndex);
        this.ready = ready;
    }

    public Ready getReady() {
        return ready;
    }
}
