package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.gateway.MessageReactionAdd;

public class MessageReactionAddAction extends AbstractGatewayWriteAction<Void> {

    private final MessageReactionAdd messageReactionAdd;

    public MessageReactionAddAction(int shardIndex, MessageReactionAdd messageReactionAdd) {
        super(shardIndex);
        this.messageReactionAdd = messageReactionAdd;
    }

    public MessageReactionAdd getMessageReactionAdd() {
        return messageReactionAdd;
    }
}
