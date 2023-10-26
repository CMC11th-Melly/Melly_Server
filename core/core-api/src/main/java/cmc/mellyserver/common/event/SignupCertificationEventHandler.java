package cmc.mellyserver.common.event;

import cmc.mellyserver.mail.EmailConstants;
import cmc.mellyserver.mail.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
class SignupCertificationEventHandler {

    private final EmailService emailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(SignupCertificationEvent event) {

        emailService.send(EmailConstants.TITLE_CERTIFICATION, event.getContent(), event.getEmail());
    }
}
