package cmc.mellyserver.notification;

public interface AlarmSender {

    void sendCommentCreatedAlarm(String nickname, Long userId);
}
