package cmc.mellyserver.mail.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import cmc.mellyserver.mail.EmailSendClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile({"local", "prod"})
@Component
@RequiredArgsConstructor
class JavaMailSendClient implements EmailSendClient {

	private final JavaMailSender mailSender;

	@Value("${email.sender}")
	private String sender;

	@Override
	public void sendMail(String subject, String content, String... receivers) {
		log.info("{} send mail to {}", subject, receivers);
		mailSender.send(createMessage(subject, content, receivers));
	}

	private SimpleMailMessage createMessage(String subject, String content, String[] receivers) {

		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(receivers);
		message.setFrom(sender);
		message.setSubject(subject);
		message.setText(content);
		return message;
	}

}
