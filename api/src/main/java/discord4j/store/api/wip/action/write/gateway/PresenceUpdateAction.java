package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.gateway.PresenceUpdate;
import discord4j.store.api.wip.util.PresenceAndUserData;

public class PresenceUpdateAction extends AbstractGatewayWriteAction<PresenceAndUserData> {

    private final PresenceUpdate presenceUpdate;

    public PresenceUpdateAction(int shardIndex, PresenceUpdate presenceUpdate) {
        super(shardIndex);
        this.presenceUpdate = presenceUpdate;
    }

    public PresenceUpdate getPresenceUpdate() {
        return presenceUpdate;
    }
}
