package cmc.mellyserver.common.event;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.domain.comment.event.SignupEvent;
import cmc.mellyserver.mail.EmailService;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SignupCompletedEventHandler {

    public static final String SIGNUP_CELEBRATION_MAIL = "회원가입 축하드립니다!";

    private final UserRepository userRepository;

    private final EmailService emailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void signupEvent(SignupEvent event) {

        User user = userRepository.findById(event.getUserId())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        emailService.send(SIGNUP_CELEBRATION_MAIL, user.getNickname(), user.getEmail());
    }

}
