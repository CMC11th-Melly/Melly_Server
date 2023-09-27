package cmc.mellyserver.notification.fcm;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FCMSendService implements MessageService {

    private final FCMTokenManageService tokenManageService;


    @Override
    public void sendCommentCreatedMessage(final Long userId, final Long memoryId, final String nickname) {

        String token = tokenManageService.getToken(userId); // FCM 푸시 알림을 보낼 유저의 토큰 조회

        Message message = Message.builder() // FCM 메세지 양식 개선
                .putData("title", "댓글 등록")
                .putData("content", nickname + "님이 메모리에 댓글을 남겼습니다.")
                .setToken(token)
                .build();

        send(message);
    }

    @Override
    public void sendCommentLikeCreatedMessage(final Long userId, final Long memoryId, final String nickname) {

        String token = tokenManageService.getToken(userId);

        Message message = Message.builder()
                .putData("title", "댓글 좋아요 알림")
                .putData("content", "메모리에 댓글에 좋아요가 달렸습니다.")
                .setToken(token)
                .build();

        send(message);
    }

    @Override
    public void sendGroupUserCreatedMemoryMessage(Long userId, Long memoryId, String nickname) {

    }

    public void send(Message message) {
        FirebaseMessaging.getInstance(FirebaseApp.getInstance()).sendAsync(message); // 비동기로 메세지 전송
    }

}
