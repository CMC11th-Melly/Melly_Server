package cmc.mellyserver.notification;

public interface NotificationService {

    void sendCommentCreatedMessage(Long memoryId, String nickname);

    void sendCommentLikeCreatedMessage(Long userId, Long memoryId, String nickname);

}
