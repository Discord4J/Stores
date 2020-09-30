package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.gateway.GuildRoleCreate;

public class GuildRoleCreateAction extends AbstractGatewayWriteAction<Void> {

    private final GuildRoleCreate guildRoleCreate;

    public GuildRoleCreateAction(int shardIndex, GuildRoleCreate guildRoleCreate) {
        super(shardIndex);
        this.guildRoleCreate = guildRoleCreate;
    }

    public GuildRoleCreate getGuildRoleCreate() {
        return guildRoleCreate;
    }
}
