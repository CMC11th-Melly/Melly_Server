package cmc.mellyserver.mellyinfra.email;

import cmc.mellyserver.mellycore.comment.application.event.SignupEvent;
import cmc.mellyserver.mellycore.common.port.message.EmailSendService;
import cmc.mellyserver.mellycore.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.mellycore.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailEventHandler {

    private final AuthenticatedUserChecker authenticatedUserChecker;

    private final EmailSendService emailSendService;

    @Async
    @TransactionalEventListener
    public void signupEvent(SignupEvent event) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(event.getUserId());
        HashMap<String, Object> map = new HashMap<>();
        map.put("nickname", user.getNickname());

        emailSendService.sendMail("회원가입 축하합니다.", map, user.getEmail());

    }
}
