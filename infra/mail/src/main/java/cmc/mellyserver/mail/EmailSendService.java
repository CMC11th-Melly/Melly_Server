package cmc.mellyserver.mail;

public interface EmailSendService {

    void sendMail(String subject, String content, String... to);
}
