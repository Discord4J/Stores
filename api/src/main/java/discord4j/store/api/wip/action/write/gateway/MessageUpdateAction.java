package discord4j.store.api.wip.action.write.gateway;

import discord4j.discordjson.json.MessageData;
import discord4j.discordjson.json.gateway.MessageUpdate;

public class MessageUpdateAction extends AbstractGatewayWriteAction<MessageData> {

    private final MessageUpdate messageUpdate;

    public MessageUpdateAction(int shardIndex, MessageUpdate messageUpdate) {
        super(shardIndex);
        this.messageUpdate = messageUpdate;
    }

    public MessageUpdate getMessageUpdate() {
        return messageUpdate;
    }
}
