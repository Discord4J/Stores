package discord4j.store.api.wip.action.read;

import discord4j.discordjson.json.RoleData;

import java.util.List;

public class GetGuildRolesAction implements ReadAction<List<RoleData>> {

    private final long guildId;

    public GetGuildRolesAction(long guildId) {
        this.guildId = guildId;
    }

    public long getGuildId() {
        return guildId;
    }
}
