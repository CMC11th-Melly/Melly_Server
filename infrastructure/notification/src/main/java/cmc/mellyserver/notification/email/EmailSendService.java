package cmc.mellyserver.notification.email;

import java.util.Map;

public interface EmailSendService {

    void sendMail(String subject, Map<String, Object> variables, String... to);
}
