package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.VoiceStateData;
import discord4j.discordjson.json.gateway.VoiceStateUpdateDispatch;

public class VoiceStateUpdateDispatchAction extends AbstractGatewayWriteAction<VoiceStateData> {

    private final VoiceStateUpdateDispatch voiceStateUpdateDispatch;

    public VoiceStateUpdateDispatchAction(int shardIndex, VoiceStateUpdateDispatch voiceStateUpdateDispatch) {
        super(shardIndex);
        this.voiceStateUpdateDispatch = voiceStateUpdateDispatch;
    }

    public VoiceStateUpdateDispatch getVoiceStateUpdateDispatch() {
        return voiceStateUpdateDispatch;
    }
}
