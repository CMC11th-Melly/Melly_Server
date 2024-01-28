package cmc.mellyserver.common.event.handler;

import static cmc.mellyserver.config.async.ExecutorConstants.*;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import cmc.mellyserver.auth.certificate.CertificateConstants;
import cmc.mellyserver.auth.certificate.CertificationCompletedEvent;
import cmc.mellyserver.mail.EmailService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class CertificationEventHandler {

    private final EmailService emailService;

    @Async(SEND_EMAIL_BEAN_NAME)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(CertificationCompletedEvent event) {
        emailService.send(CertificateConstants.TITLE_CERTIFICATION, event.content(), event.email());
    }
}
