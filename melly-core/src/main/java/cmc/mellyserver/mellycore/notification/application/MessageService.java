package cmc.mellyserver.mellycore.notification.application;

public interface MessageService {

    void sendCommentCreatedMessage(String email, String nickname);

    void sendCommentLikeCreatedMessage(String email, String nickname);
}
