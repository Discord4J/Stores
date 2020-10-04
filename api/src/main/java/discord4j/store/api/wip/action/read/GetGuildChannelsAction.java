package discord4j.store.api.wip.action.read;

import discord4j.discordjson.json.ChannelData;
import discord4j.store.api.wip.util.PossiblyIncompleteList;

public class GetGuildChannelsAction implements ReadAction<PossiblyIncompleteList<ChannelData>> {

    private final long guildId;

    public GetGuildChannelsAction(long guildId) {
        this.guildId = guildId;
    }

    public long getGuildId() {
        return guildId;
    }
}
