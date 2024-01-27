package cmc.mellyserver.notification.fcm;

import org.springframework.stereotype.Component;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;

import cmc.mellyserver.dbredis.repository.RedisRepository;
import cmc.mellyserver.notification.AlarmSender;
import cmc.mellyserver.notification.constants.AlarmConstants;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class FCMSender implements AlarmSender {

    private static final String TITLE = "title";
    private static final String CONTENT = "content";

    private final RedisRepository repository;

    @Override
    public void sendCommentCreatedMessage(String nickname, Long userId) {

        String token = repository.get(userId.toString());

        Message message = createMessage(AlarmConstants.COMMENT_CREATED_TITLE,
            nickname + AlarmConstants.COMMENT_CREATED_CONTENT, token);
        send(message);
    }

    public void send(Message message) {
        FirebaseMessaging.getInstance(FirebaseApp.getInstance()).sendAsync(message);
    }

    private Message createMessage(String title, String content, String token) {
        return Message.builder().putData(TITLE, title).
            putData(CONTENT, content)
            .setToken(token).build();
    }
}
