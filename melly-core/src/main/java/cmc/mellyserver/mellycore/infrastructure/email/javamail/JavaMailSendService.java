package cmc.mellyserver.mellycore.infrastructure.email.javamail;

import cmc.mellyserver.mellycore.infrastructure.email.EmailSendService;
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


        String content = variables.get("content").toString(); // 메일 내용 만들어내기

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("jemin3161@naver.com");
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
}
