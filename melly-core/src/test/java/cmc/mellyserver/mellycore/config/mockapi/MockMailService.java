package cmc.mellyserver.mellycore.config.mockapi;

import cmc.mellyserver.mellycore.infrastructure.email.EmailSendService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Profile("test")
public class MockMailService implements EmailSendService {

    @Override
    public void sendMail(String subject, Map<String, Object> variables, String... to) {

    }
}
