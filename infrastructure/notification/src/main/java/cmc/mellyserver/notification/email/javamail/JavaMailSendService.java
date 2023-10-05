package cmc.mellyserver.notification.email.javamail;

import cmc.mellyserver.notification.email.EmailSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@Profile({"local", "prod"})
@RequiredArgsConstructor
public class JavaMailSendService implements EmailSendService {

    private final JavaMailSender mailSender;

    @Override
    public void sendMail(String subject, Map<String, Object> variables, String... to) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setFrom("jemin3161@naver.com");
        message.setSubject(subject);
        message.setText(variables.get("content").toString());

        mailSender.send(message);
    }
}
