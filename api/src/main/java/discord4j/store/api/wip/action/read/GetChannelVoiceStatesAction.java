package discord4j.store.api.wip.action.read;

import discord4j.discordjson.json.VoiceStateData;
import discord4j.store.api.wip.util.PossiblyIncompleteList;

public class GetChannelVoiceStatesAction implements ReadAction<PossiblyIncompleteList<VoiceStateData>> {

    private final long channelId;

    public GetChannelVoiceStatesAction(long channelId) {
        this.channelId = channelId;
    }

    public long getChannelId() {
        return channelId;
    }
}
