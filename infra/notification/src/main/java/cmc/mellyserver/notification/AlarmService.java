package cmc.mellyserver.notification;

public interface AlarmService {

    void sendCommentCreatedMessage(Long memoryId, String nickname);

    void sendCommentLikeCreatedMessage(Long userId, Long memoryId, String nickname);

}
