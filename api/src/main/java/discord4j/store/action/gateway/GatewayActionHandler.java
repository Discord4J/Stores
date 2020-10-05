package discord4j.store.action.gateway;

import discord4j.discordjson.json.*;
import discord4j.store.api.wip.handler.ActionMapper;
import discord4j.store.api.wip.util.PresenceAndUserData;
import reactor.core.publisher.Mono;

import java.util.List;

public interface GatewayActionHandler {

    static ActionMapper mapper(GatewayActionHandler handler) {
        return ActionMapper.create()
                .map(ChannelCreateAction.class, handler::handleChannelCreateAction)
                .map(ChannelDeleteAction.class, handler::handleChannelDeleteAction)
                .map(ChannelUpdateAction.class, handler::handleChannelUpdateAction)
                .map(GuildCreateAction.class, handler::handleGuildCreateAction)
                .map(GuildDeleteAction.class, handler::handleGuildDeleteAction)
                .map(GuildEmojisUpdateAction.class, handler::handleGuildEmojisUpdateAction)
                .map(GuildMemberAddAction.class, handler::handleGuildMemberAddAction)
                .map(GuildMemberRemoveAction.class, handler::handleGuildMemberRemoveAction)
                .map(GuildMembersChunkAction.class, handler::handleGuildMembersChunkAction)
                .map(GuildMemberUpdateAction.class, handler::handleGuildMemberUpdateAction)
                .map(GuildRoleCreateAction.class, handler::handleGuildRoleCreateAction)
                .map(GuildRoleDeleteAction.class, handler::handleGuildRoleDeleteAction)
                .map(GuildRoleUpdateAction.class, handler::handleGuildRoleUpdateAction)
                .map(GuildUpdateAction.class, handler::handleGuildUpdateAction)
                .map(InvalidateShardAction.class, handler::handleInvalidateShardAction)
                .map(MessageCreateAction.class, handler::handleMessageCreateAction)
                .map(MessageDeleteAction.class, handler::handleMessageDeleteAction)
                .map(MessageDeleteBulkAction.class, handler::handleMessageDeleteBulkAction)
                .map(MessageReactionAddAction.class, handler::handleMessageReactionAddAction)
                .map(MessageReactionRemoveAction.class, handler::handleMessageReactionRemoveAction)
                .map(MessageReactionRemoveAllAction.class, handler::handleMessageReactionRemoveAllAction)
                .map(MessageReactionRemoveEmojiAction.class, handler::handleMessageReactionRemoveEmojiAction)
                .map(MessageUpdateAction.class, handler::handleMessageUpdateAction)
                .map(PresenceUpdateAction.class, handler::handlePresenceUpdateAction)
                .map(ReadyAction.class, handler::handleReadyAction)
                .map(UserUpdateAction.class, handler::handleUserUpdateAction)
                .map(VoiceStateUpdateDispatchAction.class, handler::handleVoiceStateUpdateDispatchAction);
    }

    default Mono<Void> handleChannelCreateAction(ChannelCreateAction action) {
        return Mono.empty();
    }

    default Mono<ChannelData> handleChannelDeleteAction(ChannelDeleteAction action) {
        return Mono.empty();
    }

    default Mono<ChannelData> handleChannelUpdateAction(ChannelUpdateAction action) {
        return Mono.empty();
    }

    default Mono<Void> handleGuildCreateAction(GuildCreateAction action) {
        return Mono.empty();
    }

    default Mono<GuildData> handleGuildDeleteAction(GuildDeleteAction action) {
        return Mono.empty();
    }

    default Mono<List<EmojiData>> handleGuildEmojisUpdateAction(GuildEmojisUpdateAction action) {
        return Mono.empty();
    }

    default Mono<Void> handleGuildMemberAddAction(GuildMemberAddAction action) {
        return Mono.empty();
    }

    default Mono<MemberData> handleGuildMemberRemoveAction(GuildMemberRemoveAction action) {
        return Mono.empty();
    }

    default Mono<Void> handleGuildMembersChunkAction(GuildMembersChunkAction action) {
        return Mono.empty();
    }

    default Mono<MemberData> handleGuildMemberUpdateAction(GuildMemberUpdateAction action) {
        return Mono.empty();
    }

    default Mono<Void> handleGuildRoleCreateAction(GuildRoleCreateAction action) {
        return Mono.empty();
    }

    default Mono<RoleData> handleGuildRoleDeleteAction(GuildRoleDeleteAction action) {
        return Mono.empty();
    }

    default Mono<RoleData> handleGuildRoleUpdateAction(GuildRoleUpdateAction action) {
        return Mono.empty();
    }

    default Mono<GuildData> handleGuildUpdateAction(GuildUpdateAction action) {
        return Mono.empty();
    }

    default Mono<Void> handleInvalidateShardAction(InvalidateShardAction action) {
        return Mono.empty();
    }

    default Mono<Void> handleMessageCreateAction(MessageCreateAction action) {
        return Mono.empty();
    }

    default Mono<MessageData> handleMessageDeleteAction(MessageDeleteAction action) {
        return Mono.empty();
    }

    default Mono<List<MessageData>> handleMessageDeleteBulkAction(MessageDeleteBulkAction action) {
        return Mono.empty();
    }

    default Mono<Void> handleMessageReactionAddAction(MessageReactionAddAction action) {
        return Mono.empty();
    }

    default Mono<Void> handleMessageReactionRemoveAction(MessageReactionRemoveAction action) {
        return Mono.empty();
    }

    default Mono<Void> handleMessageReactionRemoveAllAction(MessageReactionRemoveAllAction action) {
        return Mono.empty();
    }

    default Mono<Void> handleMessageReactionRemoveEmojiAction(MessageReactionRemoveEmojiAction action) {
        return Mono.empty();
    }

    default Mono<MessageData> handleMessageUpdateAction(MessageUpdateAction action) {
        return Mono.empty();
    }

    default Mono<PresenceAndUserData> handlePresenceUpdateAction(PresenceUpdateAction action) {
        return Mono.empty();
    }

    default Mono<Void> handleReadyAction(ReadyAction action) {
        return Mono.empty();
    }

    default Mono<UserData> handleUserUpdateAction(UserUpdateAction action) {
        return Mono.empty();
    }

    default Mono<VoiceStateData> handleVoiceStateUpdateDispatchAction(VoiceStateUpdateDispatchAction action) {
        return Mono.empty();
    }
}
