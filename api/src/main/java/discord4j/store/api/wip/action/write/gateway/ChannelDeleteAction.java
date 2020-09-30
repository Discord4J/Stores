package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.ChannelData;
import discord4j.discordjson.json.gateway.ChannelDelete;

public class ChannelDeleteAction extends AbstractGatewayWriteAction<ChannelData> {

    private final ChannelDelete channelCreate;

    public ChannelDeleteAction(int shardIndex, ChannelDelete channelCreate) {
        super(shardIndex);
        this.channelCreate = channelCreate;
    }

    public ChannelDelete getChannelDelete() {
        return channelCreate;
    }
}
