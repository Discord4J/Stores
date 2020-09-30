package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.gateway.MessageReactionRemoveEmoji;

public class MessageReactionRemoveEmojiAction extends AbstractGatewayWriteAction<Void> {

    private final MessageReactionRemoveEmoji messageReactionRemoveEmoji;

    public MessageReactionRemoveEmojiAction(int shardIndex, MessageReactionRemoveEmoji messageReactionRemoveEmoji) {
        super(shardIndex);
        this.messageReactionRemoveEmoji = messageReactionRemoveEmoji;
    }

    public MessageReactionRemoveEmoji getMessageReactionRemoveEmoji() {
        return messageReactionRemoveEmoji;
    }
}
