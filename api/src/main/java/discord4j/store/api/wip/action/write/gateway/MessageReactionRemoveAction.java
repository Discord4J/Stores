package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.gateway.MessageReactionRemove;

public class MessageReactionRemoveAction extends AbstractGatewayWriteAction<Void> {

    private final MessageReactionRemove messageReactionRemove;

    public MessageReactionRemoveAction(int shardIndex, MessageReactionRemove messageReactionRemove) {
        super(shardIndex);
        this.messageReactionRemove = messageReactionRemove;
    }

    public MessageReactionRemove getMessageReactionRemove() {
        return messageReactionRemove;
    }
}
