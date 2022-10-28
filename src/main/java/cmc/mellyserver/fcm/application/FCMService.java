package cmc.mellyserver.fcm.application;

import cmc.mellyserver.fcm.dto.FCMMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {

    private static final String API_URL = "https://fcm.googleapis.com/v1/projects/프로젝트id/messages:send";
    private final ObjectMapper objectMapper;


    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/cocotalk_firebase_service_key.json";
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    public void sendMessageTo(String targetToken, String title, String body) throws IOException
    {
        String message = makeMessage(targetToken,title,body);
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

    private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException
    {
        FCMMessage fcmMessage = FCMMessage.builder()
                .message(FCMMessage.Message.builder()
                        .token(targetToken)
                        .notification(FCMMessage.Notification.builder()
                                .title(title).body(body).image(null).build()
                        ).build()).validateOnly(false).build();
        return objectMapper.writeValueAsString(fcmMessage);
    }
}
