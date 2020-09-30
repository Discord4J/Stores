package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.MemberData;
import discord4j.discordjson.json.gateway.GuildMemberUpdate;

public class GuildMemberUpdateAction extends AbstractGatewayWriteAction<MemberData> {

    private final GuildMemberUpdate guildMemberUpdate;

    public GuildMemberUpdateAction(int shardIndex, GuildMemberUpdate guildMemberUpdate) {
        super(shardIndex);
        this.guildMemberUpdate = guildMemberUpdate;
    }

    public GuildMemberUpdate getGuildMemberUpdate() {
        return guildMemberUpdate;
    }
}
