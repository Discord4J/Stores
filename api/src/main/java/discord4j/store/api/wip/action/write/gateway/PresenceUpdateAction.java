package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.PresenceData;
import discord4j.discordjson.json.gateway.PresenceUpdate;

public class PresenceUpdateAction extends AbstractGatewayWriteAction<PresenceData> {

    private final PresenceUpdate presenceUpdate;

    public PresenceUpdateAction(int shardIndex, PresenceUpdate presenceUpdate) {
        super(shardIndex);
        this.presenceUpdate = presenceUpdate;
    }

    public PresenceUpdate getPresenceUpdate() {
        return presenceUpdate;
    }
}
