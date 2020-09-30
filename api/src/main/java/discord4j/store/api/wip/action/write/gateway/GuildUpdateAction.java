package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.GuildData;
import discord4j.discordjson.json.gateway.GuildUpdate;

public class GuildUpdateAction extends AbstractGatewayWriteAction<GuildData> {

    private final GuildUpdate guildUpdate;

    public GuildUpdateAction(int shardIndex, GuildUpdate guildUpdate) {
        super(shardIndex);
        this.guildUpdate = guildUpdate;
    }

    public GuildUpdate getGuildUpdate() {
        return guildUpdate;
    }
}
