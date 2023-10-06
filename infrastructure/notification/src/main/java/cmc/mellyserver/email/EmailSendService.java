package cmc.mellyserver.email;

public interface EmailSendService {

    void sendMail(String subject, String content, String... to);
}
