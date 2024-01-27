package cmc.mellyserver.notification;

public interface AlarmService {

    void sendCommentCreatedMessage(String nickname, String token);
}
