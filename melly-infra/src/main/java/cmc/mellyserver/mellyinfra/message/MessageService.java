package cmc.mellyserver.mellyinfra.message;

public interface MessageService {

    void sendCommentCreatedMessage(String email, String nickname);

    void sendCommentLikeCreatedMessage(String email, String nickname);
}
