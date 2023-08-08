package cmc.mellyserver.mellyinfra.email;

import org.springframework.stereotype.Service;

@Service
public class AWSSesSendService {

//    private final AmazonSimpleEmailService amazonSimpleEmailService;
//
//    private final TemplateEngine htmlTemplateEngine;
//
//    private final String from;
//
//    public AWSSesSendService(
//            AmazonSimpleEmailService amazonSimpleEmailService,
//            TemplateEngine htmlTemplateEngine,
//            @Value("${aws.ses.from}") String from
//    ) {
//        this.amazonSimpleEmailService = amazonSimpleEmailService;
//        this.htmlTemplateEngine = htmlTemplateEngine;
//        this.from = from;
//    }
//
//    @Override
//    public void sendMail(String subject, Map<String, Object> variables, String... to) {
//
//        String content = htmlTemplateEngine.process("index", createContext(variables));
//
//        SendEmailRequest sendEmailRequest = createSendEmailRequest(subject, content, to);
//
//        amazonSimpleEmailService.sendEmail(sendEmailRequest);
//    }
//
//    private Context createContext(Map<String, Object> variables) {
//        Context context = new Context();
//        context.setVariables(variables);
//
//        return context;
//    }
//
//    private SendEmailRequest createSendEmailRequest(String subject, String content, String... to) {
//        return new SendEmailRequest()
//                .withDestination(new Destination().withToAddresses(to))
//                .withSource(from)
//                .withMessage(new Message()
//                        .withSubject(new Content().withCharset(StandardCharsets.UTF_8.name()).withData(subject))
//                        .withBody(new Body().withHtml(new Content().withCharset(StandardCharsets.UTF_8.name()).withData(content)))
//                );
//    }
}

