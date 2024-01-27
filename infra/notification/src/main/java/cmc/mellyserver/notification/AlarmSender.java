package cmc.mellyserver.notification;

public interface AlarmSender {

    void sendCommentCreatedMessage(String nickname, Long userId);
}
