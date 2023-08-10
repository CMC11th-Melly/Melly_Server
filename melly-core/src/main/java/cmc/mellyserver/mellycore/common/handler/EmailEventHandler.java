package cmc.mellyserver.mellycore.common.handler;


import cmc.mellyserver.mellycore.comment.application.event.SignupEvent;
import cmc.mellyserver.mellycore.infrastructure.email.EmailSendService;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailEventHandler {

    public static final String SIGNUP_CELEBRATION_MAIL = "회원가입 축하드립니다!";

    private final UserRepository userRepository;

    private final EmailSendService emailSendService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void signupEvent(SignupEvent event) {

        User user = userRepository.getById(event.getUserId());

        HashMap<String, Object> map = new HashMap<>();
        map.put("nickname", user.getNickname());
        emailSendService.sendMail(SIGNUP_CELEBRATION_MAIL, map, user.getEmail());
    }
}
