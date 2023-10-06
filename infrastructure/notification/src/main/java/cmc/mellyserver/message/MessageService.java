package cmc.mellyserver.message;


public interface MessageService {

    void sendCommentCreatedMessage(final Long userId, final Long memoryId, final String nickname);

    void sendCommentLikeCreatedMessage(final Long userId, final Long memoryId, final String nickname);
}
