package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.UserData;
import discord4j.discordjson.json.gateway.UserUpdate;

public class UserUpdateAction extends AbstractGatewayWriteAction<UserData> {

    private final UserUpdate userUpdate;

    public UserUpdateAction(int shardIndex, UserUpdate userUpdate) {
        super(shardIndex);
        this.userUpdate = userUpdate;
    }

    public UserUpdate getUserUpdate() {
        return userUpdate;
    }
}
