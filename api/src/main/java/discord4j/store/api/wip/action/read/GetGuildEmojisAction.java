package discord4j.store.api.wip.action.read;

import discord4j.discordjson.json.EmojiData;
import discord4j.store.api.wip.util.PossiblyIncompleteList;

public class GetGuildEmojisAction implements ReadAction<PossiblyIncompleteList<EmojiData>> {

    private final long guildId;

    public GetGuildEmojisAction(long guildId) {
        this.guildId = guildId;
    }

    public long getGuildId() {
        return guildId;
    }
}
