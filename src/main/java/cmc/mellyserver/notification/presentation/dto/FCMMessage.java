package cmc.mellyserver.notification.presentation.dto;

import cmc.mellyserver.notification.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;


@Data
@AllArgsConstructor
@Builder
public class FCMMessage {
    private boolean validateOnly;
    private Message message;



    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message{
        private Notification notification;
        private String token;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification{
        private NotificationType title;
        private String body;
        private String image;
    }

}
