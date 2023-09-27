package cmc.mellyserver.notification.fcm;


public interface MessageService {

    void sendCommentCreatedMessage(final Long userId, final Long memoryId, final String nickname);

    void sendCommentLikeCreatedMessage(final Long userId, final Long memoryId, final String nickname);

    void sendGroupUserCreatedMemoryMessage(final Long userId, final Long memoryId, final String nickname);
}
