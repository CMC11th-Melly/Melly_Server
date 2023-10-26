package cmc.mellyserver.common.event;

import cmc.mellyserver.mail.EmailSendService;
import cmc.mellyserver.mail.constant.EmailConstants;
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

    private final EmailSendService emailSendService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(SignupCertificationEvent event) {

        emailSendService.sendMail(EmailConstants.TITLE_CERTIFICATION, event.getContent(), event.getEmail());
    }
}
