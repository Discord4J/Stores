package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.gateway.GuildMemberAdd;

public class GuildMemberAddAction extends AbstractGatewayWriteAction<Void> {

    private final GuildMemberAdd guildMemberAdd;

    public GuildMemberAddAction(int shardIndex, GuildMemberAdd guildMemberAdd) {
        super(shardIndex);
        this.guildMemberAdd = guildMemberAdd;
    }

    public GuildMemberAdd getGuildMemberAdd() {
        return guildMemberAdd;
    }
}
