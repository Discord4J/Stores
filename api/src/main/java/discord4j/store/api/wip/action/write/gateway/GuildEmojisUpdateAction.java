package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.EmojiData;
import discord4j.discordjson.json.gateway.GuildEmojisUpdate;

import java.util.Set;

public class GuildEmojisUpdateAction extends AbstractGatewayWriteAction<Set<EmojiData>> {

    private final GuildEmojisUpdate guildEmojisUpdate;

    public GuildEmojisUpdateAction(int shardIndex, GuildEmojisUpdate guildEmojisUpdate) {
        super(shardIndex);
        this.guildEmojisUpdate = guildEmojisUpdate;
    }

    public GuildEmojisUpdate getGuildEmojisUpdate() {
        return guildEmojisUpdate;
    }
}
