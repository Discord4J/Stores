package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.gateway.MessageCreate;

public class MessageCreateAction extends AbstractGatewayWriteAction<Void> {

    private final MessageCreate messageCreate;

    public MessageCreateAction(int shardIndex, MessageCreate messageCreate) {
        super(shardIndex);
        this.messageCreate = messageCreate;
    }

    public MessageCreate getMessageCreate() {
        return messageCreate;
    }
}
