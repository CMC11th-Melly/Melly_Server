package cmc.mellyserver.common.event.handler;

import static cmc.mellyserver.config.async.ExecutorConstants.*;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import cmc.mellyserver.auth.common.constant.AuthConstants;
import cmc.mellyserver.auth.common.event.SignupEvent;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.user.UserReader;
import cmc.mellyserver.mail.EmailService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SignupCompletedEventHandler {

    private final UserReader userReader;

    private final EmailService emailService;

    @Async(SEND_EMAIL_BEAN_NAME)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void signupEvent(SignupEvent event) {
        User user = userReader.findById(event.getUserId());
        emailService.send(AuthConstants.SIGNUP_CELEBRATION_MAIL, user.getNickname(), user.getEmail());
    }

}
