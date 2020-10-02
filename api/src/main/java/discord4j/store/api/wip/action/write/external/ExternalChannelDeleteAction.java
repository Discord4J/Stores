package discord4j.store.api.wip.action.write.external;

import discord4j.discordjson.json.ChannelData;

public class ExternalChannelDeleteAction implements ExternalWriteAction<ChannelData> {

    private final long channelId;

    public ExternalChannelDeleteAction(long channelId) {
        this.channelId = channelId;
    }

    public long getChannelId() {
        return channelId;
    }
}
