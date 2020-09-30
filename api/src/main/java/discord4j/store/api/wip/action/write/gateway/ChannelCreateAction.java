package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.gateway.ChannelCreate;

public class ChannelCreateAction extends AbstractGatewayWriteAction<Void> {

    private final ChannelCreate channelCreate;

    public ChannelCreateAction(int shardIndex, ChannelCreate channelCreate) {
        super(shardIndex);
        this.channelCreate = channelCreate;
    }

    public ChannelCreate getChannelCreate() {
        return channelCreate;
    }
}
