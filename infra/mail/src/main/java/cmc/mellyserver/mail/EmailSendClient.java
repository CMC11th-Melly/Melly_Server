package cmc.mellyserver.mail;

public interface EmailSendClient {

    void sendMail(String subject, String content, String... to);
}
