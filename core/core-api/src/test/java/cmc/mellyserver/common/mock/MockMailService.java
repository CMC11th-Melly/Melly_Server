package cmc.mellyserver.common.mock;

import cmc.mellyserver.email.EmailSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@Profile("test")
public class MockMailService implements EmailSendService {

    @Override
    public void sendMail(String subject, Map<String, Object> variables, String... to) {

    }
}
