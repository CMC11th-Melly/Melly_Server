package cmc.mellyserver.common.mock;

import cmc.mellyserver.mail.EmailSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("test")
public class MockMailService implements EmailSendService {


    @Override
    public void sendMail(String subject, String content, String... to) {

    }
}
