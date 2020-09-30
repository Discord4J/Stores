package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.RoleData;
import discord4j.discordjson.json.gateway.GuildRoleUpdate;

public class GuildRoleUpdateAction extends AbstractGatewayWriteAction<RoleData> {

    private final GuildRoleUpdate guildRoleUpdate;

    public GuildRoleUpdateAction(int shardIndex, GuildRoleUpdate guildRoleUpdate) {
        super(shardIndex);
        this.guildRoleUpdate = guildRoleUpdate;
    }

    public GuildRoleUpdate getGuildRoleUpdate() {
        return guildRoleUpdate;
    }
}
