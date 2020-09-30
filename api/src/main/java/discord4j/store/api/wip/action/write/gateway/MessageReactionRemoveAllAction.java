package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.gateway.MessageReactionRemoveAll;

public class MessageReactionRemoveAllAction extends AbstractGatewayWriteAction<Void> {

    private final MessageReactionRemoveAll messageReactionRemoveAll;

    public MessageReactionRemoveAllAction(int shardIndex, MessageReactionRemoveAll messageReactionRemoveAll) {
        super(shardIndex);
        this.messageReactionRemoveAll = messageReactionRemoveAll;
    }

    public MessageReactionRemoveAll getMessageReactionRemoveAll() {
        return messageReactionRemoveAll;
    }
}
