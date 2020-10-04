package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.gateway.RequestGuildMembers;

public class RequestMembersAction extends AbstractGatewayWriteAction<Void> {

    private final RequestGuildMembers requestGuildMembers;

    public RequestMembersAction(int shardIndex, RequestGuildMembers requestGuildMembers) {
        super(shardIndex);
        this.requestGuildMembers = requestGuildMembers;
    }

    public RequestGuildMembers getRequestGuildMembers() {
        return requestGuildMembers;
    }
}
