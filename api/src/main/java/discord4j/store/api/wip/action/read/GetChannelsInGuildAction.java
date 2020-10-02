package discord4j.store.api.wip.action.read;

import discord4j.discordjson.json.ChannelData;

import java.util.List;

public class GetChannelsInGuildAction implements ReadAction<List<ChannelData>> {

    private final long guildId;

    public GetChannelsInGuildAction(long guildId) {
        this.guildId = guildId;
    }

    public long getGuildId() {
        return guildId;
    }
}
