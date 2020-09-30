package discord4j.store.api.wip.action.write.external;

import discord4j.discordjson.json.ChannelData;

public class ExternalChannelSaveAction implements ExternalWriteAction<ChannelData> {

    private final ChannelData channelData;

    public ExternalChannelSaveAction(ChannelData channelData) {
        this.channelData = channelData;
    }

    public ChannelData getChannelData() {
        return channelData;
    }
}
