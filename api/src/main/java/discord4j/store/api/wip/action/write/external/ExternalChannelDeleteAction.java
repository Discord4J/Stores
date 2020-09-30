package discord4j.store.api.wip.action.write.external;

import discord4j.common.util.Snowflake;
import discord4j.discordjson.json.ChannelData;

public class ExternalChannelDeleteAction implements ExternalWriteAction<ChannelData> {

    private final Snowflake channelId;

    public ExternalChannelDeleteAction(Snowflake channelId) {
        this.channelId = channelId;
    }

    public Snowflake getChannelId() {
        return channelId;
    }
}
