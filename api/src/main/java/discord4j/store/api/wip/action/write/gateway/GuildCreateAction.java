package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.gateway.GuildCreate;

public class GuildCreateAction extends AbstractGatewayWriteAction<Void> {

    private final GuildCreate guildCreate;

    public GuildCreateAction(int shardIndex, GuildCreate guildCreate) {
        super(shardIndex);
        this.guildCreate = guildCreate;
    }

    public GuildCreate getGuildCreate() {
        return guildCreate;
    }
}
