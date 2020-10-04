package discord4j.store.api.wip.action.read;

import discord4j.discordjson.json.PresenceData;
import discord4j.store.api.wip.util.PossiblyIncompleteList;

public class GetGuildPresencesAction implements ReadAction<PossiblyIncompleteList<PresenceData>> {

    private final long guildId;

    public GetGuildPresencesAction(long guildId) {
        this.guildId = guildId;
    }

    public long getGuildId() {
        return guildId;
    }
}
