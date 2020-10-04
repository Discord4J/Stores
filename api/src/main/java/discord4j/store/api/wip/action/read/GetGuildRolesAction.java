package discord4j.store.api.wip.action.read;

import discord4j.discordjson.json.RoleData;
import discord4j.store.api.wip.util.PossiblyIncompleteList;

public class GetGuildRolesAction implements ReadAction<PossiblyIncompleteList<RoleData>> {

    private final long guildId;

    public GetGuildRolesAction(long guildId) {
        this.guildId = guildId;
    }

    public long getGuildId() {
        return guildId;
    }
}
