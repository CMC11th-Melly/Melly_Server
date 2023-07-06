package cmc.mellyserver.mellyinfra.alarm;

import cmc.mellyserver.mellycommon.enums.NotificationType;
import cmc.mellyserver.mellycore.notification.application.NotificationService;
import cmc.mellyserver.mellycore.notification.domain.FCMMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FCMSender {

    private static final String API_URL = "https://fcm.googleapis.com/v1/projects/melly-fdd90/messages:send";
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/melly-fdd90-firebase-adminsdk-a139l-bd9a957ecb.json";
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
                        new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    @Transactional
    public void sendMessageTo(String targetToken, NotificationType notificationType, String body, Long userSeq,
                              Long memoryId) throws IOException {

        notificationService.createNotification(notificationType, body, userSeq, memoryId);
        String message = makeMessage(targetToken, notificationType, body);
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), message);
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = okHttpClient.newCall(request).execute();
        log.info(response.body().string());

    }

    private String makeMessage(String targetToken, NotificationType notificationType, String body) throws
            JsonProcessingException {
        FCMMessage fcmMessage = FCMMessage.builder()
                .message(FCMMessage.Message.builder()
                        .token(targetToken)
                        .notification(FCMMessage.Notification.builder()
                                .title(notificationType).body(body).image(null).build()
                        ).build()).validateOnly(false).build();
        return objectMapper.writeValueAsString(fcmMessage);
    }
}
