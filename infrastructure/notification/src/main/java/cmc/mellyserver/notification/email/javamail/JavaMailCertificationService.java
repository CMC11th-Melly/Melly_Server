package cmc.mellyserver.notification.email.javamail;

import cmc.mellyserver.notification.email.certification.EmailCertificationDao;
import cmc.mellyserver.notification.email.certification.EmailCertificationRequest;
import cmc.mellyserver.notification.email.certification.EmailCertificationService;
import cmc.mellyserver.notification.email.certification.RandomNumberGenerator;
import cmc.mellyserver.notification.email.constant.EmailConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Profile({"local", "prod"})
@Repository
public class JavaMailCertificationService implements EmailCertificationService {

    private final JavaMailSender mailSender;
    private final EmailCertificationDao emailCertificationNumberDao;

    /**
     * 인증 이메일 전송 기능
     */
    public void sendEmailForCertification(String email) {

        String content = RandomNumberGenerator.makeRandomNumber(); // 인증에 사용될 랜덤 넘버를 생성합니다

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("jemin3161@naver.com");
        message.setSubject(EmailConstants.TITLE_CERTIFICATION);
        message.setText(content);
        mailSender.send(message);

        emailCertificationNumberDao.saveEmailCertificationNumber(email, content);
    }


    // 인증번호 일치 여부 확인
    public void verifyEmail(EmailCertificationRequest requestDto) {
        if (isVerify(requestDto)) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }

        // 인증하고 나면 레디스에서 삭제하기
        emailCertificationNumberDao.removeEmailCertificationNumber(requestDto.getEmail());
    }


    // 인증번호 일치 여부 확인 내부 로직
    private boolean isVerify(EmailCertificationRequest requestDto) {
        return !(emailCertificationNumberDao.hasKey(requestDto.getEmail()) && emailCertificationNumberDao.getEmailCertificationNumber(requestDto.getEmail()).equals(requestDto.getCertificationNumber()));
    }
}