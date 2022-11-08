package cmc.mellyserver.invitation.presentation;

import cmc.mellyserver.invitation.application.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InvitationController {

    private final EmailSender emailSender;

    @PostMapping("/mail")
    public void execMail()
    {
        emailSender.sendEmail("jemin3161@naver.com","메일 전송 테스트","테스트 중입니다.");
    }


}
