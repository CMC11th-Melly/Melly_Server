package cmc.mellyserver.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Message<T> {
    private String token;
    private Notification notification;
    private T data;

    @Data
    static class Notification{
        String title;
        String body;
        public Notification(String title, String body)
        {
            this.title = title;
            this.body = body;
        }

    }

}
