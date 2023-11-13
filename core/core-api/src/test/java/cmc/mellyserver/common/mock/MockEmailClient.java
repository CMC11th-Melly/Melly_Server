package cmc.mellyserver.common.mock;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import cmc.mellyserver.mail.EmailSendClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("test")
public class MockEmailClient implements EmailSendClient {

	@Override
	public void sendMail(String subject, String content, String... to) {
		log.info("send mail by mockEmailClient");
	}

}
