package cmc.mellyserver.message.fcm;

import cmc.mellyserver.dbredis.repository.FcmTokenRepository;
import cmc.mellyserver.message.MessageService;
import cmc.mellyserver.message.constants.NotificationConstants;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class FCMSendService implements MessageService {

    private final FcmTokenRepository tokenRepository;


    @Override
    public void sendCommentCreatedMessage(final Long userId, final Long memoryId, final String nickname) {

        String token = tokenRepository.getToken(userId.toString());
        Message message = createMessage(NotificationConstants.COMMENT_CREATED_TITLE, nickname + NotificationConstants.COMMENT_CREATED_CONTENT, token);
        send(message);
    }

    @Override
    public void sendCommentLikeCreatedMessage(final Long userId, final Long memoryId, final String nickname) {

        String token = tokenRepository.getToken(userId.toString());
        Message message = createMessage(NotificationConstants.COMMENT_LIKE_NOTI_TITLE, nickname + NotificationConstants.COMMENT_LIKE_NOTI_CONTENT, token);
        send(message);
    }


    private Message createMessage(String title, String content, String token) {
        return Message.builder()
                .putData("title", title)
                .putData("content", content)
                .setToken(token)
                .build();
    }

    public void send(Message message) {
        FirebaseMessaging.getInstance(FirebaseApp.getInstance()).sendAsync(message); // 비동기로 메세지 전송
    }

}