package cmc.mellyserver.email.javamail;

import cmc.mellyserver.email.EmailSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile({"local", "prod"})
@RequiredArgsConstructor
class JavaMailSendService implements EmailSendService {

    private final JavaMailSender mailSender;

    @Value("${email.sender}")
    private String sender;

    @Override
    public void sendMail(String subject, String content, String... receivers) {

        mailSender.send(createMessage(subject, content, receivers));
    }

    private SimpleMailMessage createMessage(String subject, String content, String[] receivers) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(receivers);
        message.setFrom(sender);
        message.setSubject(subject);
        message.setText(content);
        return message;
    }
}
