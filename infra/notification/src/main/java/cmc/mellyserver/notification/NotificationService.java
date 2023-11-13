package cmc.mellyserver.notification;

public interface NotificationService {

    void sendCommentCreatedMessage(final Long memoryId, final String nickname);

    void sendCommentLikeCreatedMessage(final Long userId, final Long memoryId, final String nickname);

}
