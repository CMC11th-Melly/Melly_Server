package cmc.mellyserver.mellyinfra.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Repository;

import static cmc.mellyserver.mellyinfra.common.constant.EmailConstants.TITLE_CERTIFICATION;
import static cmc.mellyserver.mellyinfra.email.RandomNumberGenerator.makeRandomNumber;

@RequiredArgsConstructor
@Repository
public class JavaMailCertificationService implements EmailCertificationService {

    private final JavaMailSender mailSender;
    private final EmailCertificationDao emailCertificationNumberDao;

    // 이메일 전송 및 인증번호 저장
    public void sendEmailForCertification(String email) {

        String randomNumber = makeRandomNumber(); // 임의의 숫자 생성
        String content = makeEmailContent(randomNumber); // 메일 내용 만들어내기

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("jemin3161@naver.com");
        message.setSubject(TITLE_CERTIFICATION);
        message.setText(content);
        mailSender.send(message);

        emailCertificationNumberDao.createEmail(email, randomNumber);
    }


    // 인증 이메일 내용 생성
    private String makeEmailContent(String certificationNumber) {
        EmailContentTemplate content = new EmailContentTemplate();
        return content.buildCertificationContent(certificationNumber);
    }

    // 인증번호 일치 여부 확인
    public void verifyEmail(EmailCertificationRequest requestDto) {
        if (isVerify(requestDto)) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }

        // 인증하고 나면 레디스에서 삭제하기
        emailCertificationNumberDao.removeEmailCertification(requestDto.getEmail());
    }


    // 인증번호 일치 여부 확인 내부 로직
    private boolean isVerify(EmailCertificationRequest requestDto) {
        return !(emailCertificationNumberDao.hasKey(requestDto.getEmail()) && emailCertificationNumberDao.getEmailCertification(requestDto.getEmail()).equals(requestDto.getCertificationNumber()));
    }
}