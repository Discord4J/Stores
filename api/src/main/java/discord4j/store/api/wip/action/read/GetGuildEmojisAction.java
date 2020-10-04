package discord4j.store.api.wip.action.read;

import discord4j.discordjson.json.EmojiData;

import java.util.List;

public class GetGuildEmojisAction implements ReadAction<List<EmojiData>> {

    private final long guildId;

    public GetGuildEmojisAction(long guildId) {
        this.guildId = guildId;
    }

    public long getGuildId() {
        return guildId;
    }
}
