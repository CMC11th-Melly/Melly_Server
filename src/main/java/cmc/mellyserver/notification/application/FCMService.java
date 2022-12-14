package cmc.mellyserver.notification.application;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryRepository;
import cmc.mellyserver.notification.domain.Notification;
import cmc.mellyserver.notification.domain.NotificationRepository;
import cmc.mellyserver.notification.domain.NotificationType;
import cmc.mellyserver.notification.presentation.dto.FCMMessage;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {

    private static final String API_URL = "https://fcm.googleapis.com/v1/projects/melly-fdd90/messages:send";
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;


    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/melly-fdd90-firebase-adminsdk-a139l-bd9a957ecb.json";
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    @Transactional
    public void sendMessageTo(String targetToken, NotificationType notificationType, String body, String uid, Long memoryId) throws IOException
    {

        notificationService.createNotification(notificationType, body, uid, memoryId);
        String message = makeMessage(targetToken,notificationType,body);
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),message);
        Request request = new Request.Builder()
                                .url(API_URL)
                                .post(requestBody)
                                .addHeader(HttpHeaders.AUTHORIZATION,"Bearer " + getAccessToken())
                                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                                .build();

        Response response = okHttpClient.newCall(request).execute();
        log.info(response.body().string());


    }

    private String makeMessage(String targetToken, NotificationType notificationType, String body) throws JsonProcessingException
    {
        FCMMessage fcmMessage = FCMMessage.builder()
                .message(FCMMessage.Message.builder()
                        .token(targetToken)
                        .notification(FCMMessage.Notification.builder()
                                .title(notificationType).body(body).image(null).build()
                        ).build()).validateOnly(false).build();
        return objectMapper.writeValueAsString(fcmMessage);
    }
}
