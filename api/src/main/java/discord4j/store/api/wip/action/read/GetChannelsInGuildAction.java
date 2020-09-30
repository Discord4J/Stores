package discord4j.store.api.wip.action.read;

import discord4j.common.util.Snowflake;
import discord4j.discordjson.json.ChannelData;

import java.util.List;

public class GetChannelsInGuildAction implements ReadAction<List<ChannelData>> {

    private final Snowflake guildId;

    public GetChannelsInGuildAction(Snowflake guildId) {
        this.guildId = guildId;
    }

    public Snowflake getGuildId() {
        return guildId;
    }
}
