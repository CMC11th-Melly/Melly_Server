package cmc.mellyserver.invitation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender sender;
    private static final String FROM_ADDRESS = "jemin03120111@gmail.com";
    public Map<String,Object> sendEmail(String toAddress,String subject, String body)
    {
        HashMap<String, Object> result = new HashMap<>();
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {

            helper.setTo(toAddress);
            helper.setFrom(FROM_ADDRESS);
            helper.setSubject(subject);
            helper.setText("www.naver.com");
            result.put("resultCode",200);
        }
        catch (MessagingException e)
        {
            result.put("resultCode",500);
        }
        sender.send(message);
        return result;
    }

}
