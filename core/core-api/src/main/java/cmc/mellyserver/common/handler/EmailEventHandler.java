package cmc.mellyserver.common.handler;


import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.domain.comment.event.SignupEvent;
import cmc.mellyserver.notification.email.EmailSendService;
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

    @Async(value = "threadPoolTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void signupEvent(SignupEvent event) {

        User user = userRepository.getById(event.getUserId());

        HashMap<String, Object> map = new HashMap<>();
        map.put("nickname", user.getNickname());
        emailSendService.sendMail(SIGNUP_CELEBRATION_MAIL, map, user.getEmail());
    }
}
