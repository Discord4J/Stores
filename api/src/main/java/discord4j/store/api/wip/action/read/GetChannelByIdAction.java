package discord4j.store.api.wip.action.read;

import discord4j.common.util.Snowflake;
import discord4j.discordjson.json.ChannelData;

public class GetChannelByIdAction implements ReadAction<ChannelData> {

    private final Snowflake channelId;

    public GetChannelByIdAction(Snowflake channelId) {
        this.channelId = channelId;
    }

    public Snowflake getChannelId() {
        return channelId;
    }
}
