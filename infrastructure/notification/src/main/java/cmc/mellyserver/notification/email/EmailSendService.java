package cmc.mellyserver.notification.email;

public interface EmailSendService {

    void sendMail(String subject, String content, String... to);
}
