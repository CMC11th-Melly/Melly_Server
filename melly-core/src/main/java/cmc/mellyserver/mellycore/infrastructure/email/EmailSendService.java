package cmc.mellyserver.mellycore.infrastructure.email;

import java.util.Map;

public interface EmailSendService {

    void sendMail(String subject, Map<String, Object> variables, String... to);
}
