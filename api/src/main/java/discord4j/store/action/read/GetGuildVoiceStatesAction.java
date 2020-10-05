package discord4j.store.action.read;

import discord4j.discordjson.json.VoiceStateData;
import discord4j.store.api.wip.util.PossiblyIncompleteList;

public class GetGuildVoiceStatesAction implements ReadAction<PossiblyIncompleteList<VoiceStateData>> {

    private final long guildId;

    public GetGuildVoiceStatesAction(long guildId) {
        this.guildId = guildId;
    }

    public long getGuildId() {
        return guildId;
    }
}
