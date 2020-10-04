package discord4j.store.api.wip.action.read;

import discord4j.discordjson.json.MemberData;

import java.util.List;

public class GetGuildMembersAction implements ReadAction<List<MemberData>> {

    private final long guildId;

    public GetGuildMembersAction(long guildId) {
        this.guildId = guildId;
    }

    public long getGuildId() {
        return guildId;
    }
}
