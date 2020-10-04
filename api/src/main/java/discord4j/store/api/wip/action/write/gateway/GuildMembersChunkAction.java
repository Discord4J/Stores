package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.gateway.GuildMembersChunk;

public class GuildMembersChunkAction extends AbstractGatewayWriteAction<Void> {

    private final GuildMembersChunk guildMembersChunk;

    public GuildMembersChunkAction(int shardIndex, GuildMembersChunk guildMembersChunk) {
        super(shardIndex);
        this.guildMembersChunk = guildMembersChunk;
    }

    public GuildMembersChunk getGuildMembersChunk() {
        return guildMembersChunk;
    }
}