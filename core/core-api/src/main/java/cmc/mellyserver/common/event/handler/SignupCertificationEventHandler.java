package cmc.mellyserver.common.event.handler;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import cmc.mellyserver.common.event.CertificationCompletedEvent;
import cmc.mellyserver.mail.EmailConstants;
import cmc.mellyserver.mail.EmailService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class SignupCertificationEventHandler {

    private final EmailService emailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(CertificationCompletedEvent event) {

        emailService.send(EmailConstants.TITLE_CERTIFICATION, event.content(), event.email());
    }

}
