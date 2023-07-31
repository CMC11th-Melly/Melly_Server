package cmc.mellyserver.mellyinfra.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static cmc.mellyserver.mellyinfra.common.constant.EmailConstants.SIGNUP_SUCCESS;

@Slf4j
@Service
@RequiredArgsConstructor
public class JavaMailSendService implements EmailSendService {

    private final JavaMailSender mailSender;

    @Override
    public void sendSignupEmail(String email, String nickname) {


        String content = makeEmailContent(nickname); // 메일 내용 만들어내기

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("jemin3161@naver.com");
        message.setSubject(SIGNUP_SUCCESS);
        message.setText(content);
        mailSender.send(message);
    }

    private String makeEmailContent(String nickname) {
        EmailContentTemplate content = new EmailContentTemplate();
        return content.buildSignupSuccessContent(nickname);
    }
}
