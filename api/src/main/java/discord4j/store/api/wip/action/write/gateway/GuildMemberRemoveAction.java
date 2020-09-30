package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.MemberData;
import discord4j.discordjson.json.gateway.GuildMemberRemove;

public class GuildMemberRemoveAction extends AbstractGatewayWriteAction<MemberData> {

    private final GuildMemberRemove guildMemberRemove;

    public GuildMemberRemoveAction(int shardIndex, GuildMemberRemove guildMemberRemove) {
        super(shardIndex);
        this.guildMemberRemove = guildMemberRemove;
    }

    public GuildMemberRemove getGuildMemberRemove() {
        return guildMemberRemove;
    }
}
