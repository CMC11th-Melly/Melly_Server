package cmc.mellyserver.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailService {

    private final EmailSendClient emailSendClient;

    public void send(String subject, String content, String... to){
        emailSendClient.sendMail(subject, content, to);
    }
}
