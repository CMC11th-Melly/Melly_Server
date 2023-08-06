package cmc.mellyserver.mellyinfra.email;

import cmc.mellyserver.mellycore.common.port.message.EmailSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class JavaMailSendService implements EmailSendService {

    private final JavaMailSender mailSender;

    @Override
    public void sendMail(String subject, Map<String, Object> variables, String... to) {


        String content = makeEmailContent(variables.get("content").toString()); // 메일 내용 만들어내기

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("jemin3161@naver.com");
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    private String makeEmailContent(String nickname) {
        EmailContentTemplate content = new EmailContentTemplate();
        return content.buildSignupSuccessContent(nickname);
    }
}
