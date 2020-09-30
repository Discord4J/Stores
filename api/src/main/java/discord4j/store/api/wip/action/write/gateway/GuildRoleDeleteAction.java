package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.RoleData;
import discord4j.discordjson.json.gateway.GuildRoleDelete;

public class GuildRoleDeleteAction extends AbstractGatewayWriteAction<RoleData> {

    private final GuildRoleDelete guildRoleDelete;

    public GuildRoleDeleteAction(int shardIndex, GuildRoleDelete guildRoleDelete) {
        super(shardIndex);
        this.guildRoleDelete = guildRoleDelete;
    }

    public GuildRoleDelete getGuildRoleDelete() {
        return guildRoleDelete;
    }
}
