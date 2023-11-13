package cmc.mellyserver.mail;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailService {

  /*
  EmailSendClient 분리 이유
  이메일을 보낸 이후에 히스토리를 DB에 저장하는 작업 같이 부가 기능이 차후에 추가될 수 있습니다.
  이메일 전송이라는 핵심 기능을 EmailSendClient에 위임함으로써 EmailService는 차후 추가될 수 있는 비즈니스 로직에 집중 가능합니다.
  이때 EmailClient는 Java Mail이 될수도 있고, AWS SES를 사용할수도 있다. 따라서 인터페이스를 구현하는 방식으로 객체지향의 다형성을 보장했습니다.
   */
  private final EmailSendClient emailSendClient;

  public void send(String subject, String content, String... to) {
	emailSendClient.sendMail(subject, content, to);
  }

}
