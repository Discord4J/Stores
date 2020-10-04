package discord4j.store.api.wip.action.read;

import discord4j.discordjson.json.UserData;

public class GetUserByIdAction implements ReadAction<UserData> {

    private final long userId;

    public GetUserByIdAction(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }
}
