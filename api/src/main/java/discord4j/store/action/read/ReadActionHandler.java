package discord4j.store.action.read;

import discord4j.discordjson.json.*;
import discord4j.store.api.wip.handler.ActionMapper;
import discord4j.store.api.wip.util.PossiblyIncompleteList;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ReadActionHandler {

    static ActionMapper mapper(ReadActionHandler handler) {
        return ActionMapper.create()
                .map(CountAction.class, handler::handleCountAction)
                .map(GetChannelByIdAction.class, handler::handleGetChannelByIdAction)
                .map(GetChannelVoiceStatesAction.class, handler::handleGetChannelVoiceStatesAction)
                .map(GetGuildByIdAction.class, handler::handleGetGuildByIdAction)
                .map(GetGuildChannelsAction.class, handler::handleGetGuildChannelsAction)
                .map(GetGuildEmojiByIdAction.class, handler::handleGetGuildEmojiByIdAction)
                .map(GetGuildEmojisAction.class, handler::handleGetGuildEmojisAction)
                .map(GetGuildMembersAction.class, handler::handleGetGuildMembersAction)
                .map(GetGuildPresencesAction.class, handler::handleGetGuildPresencesAction)
                .map(GetGuildRolesAction.class, handler::handleGetGuildRolesAction)
                .map(GetGuildsAction.class, handler::handleGetGuildsAction)
                .map(GetGuildVoiceStatesAction.class, handler::handleGetGuildVoiceStatesAction)
                .map(GetMemberByIdAction.class, handler::handleGetMemberByIdAction)
                .map(GetMessageByIdAction.class, handler::handleGetMessageByIdAction)
                .map(GetPresenceByIdAction.class, handler::handleGetPresenceByIdAction)
                .map(GetRoleByIdAction.class, handler::handleGetRoleByIdAction)
                .map(GetUserByIdAction.class, handler::handleGetUserByIdAction)
                .map(GetUsersAction.class, handler::handleGetUsersAction)
                .map(GetVoiceStateByIdAction.class, handler::handleGetVoiceStateByIdAction);
    }

    default Mono<Long> handleCountAction(CountAction action) {
        return Mono.empty();
    }

    default Mono<ChannelData> handleGetChannelByIdAction(GetChannelByIdAction action) {
        return Mono.empty();
    }

    default Mono<PossiblyIncompleteList<VoiceStateData>> handleGetChannelVoiceStatesAction(GetChannelVoiceStatesAction action) {
        return Mono.empty();
    }

    default Mono<GuildData> handleGetGuildByIdAction(GetGuildByIdAction action) {
        return Mono.empty();
    }

    default Mono<PossiblyIncompleteList<ChannelData>> handleGetGuildChannelsAction(GetGuildChannelsAction action) {
        return Mono.empty();
    }

    default Mono<EmojiData> handleGetGuildEmojiByIdAction(GetGuildEmojiByIdAction action) {
        return Mono.empty();
    }

    default Mono<PossiblyIncompleteList<EmojiData>> handleGetGuildEmojisAction(GetGuildEmojisAction action) {
        return Mono.empty();
    }

    default Mono<PossiblyIncompleteList<MemberData>> handleGetGuildMembersAction(GetGuildMembersAction action) {
        return Mono.empty();
    }

    default Mono<PossiblyIncompleteList<PresenceData>> handleGetGuildPresencesAction(GetGuildPresencesAction action) {
        return Mono.empty();
    }

    default Mono<PossiblyIncompleteList<RoleData>> handleGetGuildRolesAction(GetGuildRolesAction action) {
        return Mono.empty();
    }

    default Mono<PossiblyIncompleteList<GuildData>> handleGetGuildsAction(GetGuildsAction action) {
        return Mono.empty();
    }

    default Mono<PossiblyIncompleteList<VoiceStateData>> handleGetGuildVoiceStatesAction(GetGuildVoiceStatesAction action) {
        return Mono.empty();
    }

    default Mono<MemberData> handleGetMemberByIdAction(GetMemberByIdAction action) {
        return Mono.empty();
    }

    default Mono<MessageData> handleGetMessageByIdAction(GetMessageByIdAction action) {
        return Mono.empty();
    }

    default Mono<PresenceData> handleGetPresenceByIdAction(GetPresenceByIdAction action) {
        return Mono.empty();
    }

    default Mono<RoleData> handleGetRoleByIdAction(GetRoleByIdAction action) {
        return Mono.empty();
    }

    default Mono<UserData> handleGetUserByIdAction(GetUserByIdAction action) {
        return Mono.empty();
    }

    default Mono<List<UserData>> handleGetUsersAction(GetUsersAction action) {
        return Mono.empty();
    }

    default Mono<VoiceStateData> handleGetVoiceStateByIdAction(GetVoiceStateByIdAction action) {
        return Mono.empty();
    }
}
