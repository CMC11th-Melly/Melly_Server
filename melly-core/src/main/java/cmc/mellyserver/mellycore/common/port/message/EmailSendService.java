package cmc.mellyserver.mellycore.common.port.message;

import java.util.Map;

public interface EmailSendService {

    void sendMail(String subject, Map<String, Object> variables, String... to);
}
