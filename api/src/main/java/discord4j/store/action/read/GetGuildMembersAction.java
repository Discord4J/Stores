package discord4j.store.action.read;

import discord4j.discordjson.json.MemberData;
import discord4j.store.api.wip.util.PossiblyIncompleteList;

public class GetGuildMembersAction implements ReadAction<PossiblyIncompleteList<MemberData>> {

    private final long guildId;

    public GetGuildMembersAction(long guildId) {
        this.guildId = guildId;
    }

    public long getGuildId() {
        return guildId;
    }
}
