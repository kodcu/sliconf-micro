package javaday.istanbul.sliconf.micro.admin;

import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.mail.IMailSendService;
import javaday.istanbul.sliconf.micro.mail.MailMessageProvider;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.template.Service.TemplateRepositoryService;
import javaday.istanbul.sliconf.micro.template.model.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
@RequiredArgsConstructor
@Component
@Profile("test")
public class EventStateSendMailTest {
    private final TemplateRepositoryService tempService;
    private final AdminMailService adminMailService;
    private final MailMessageProvider mailMessageProvider;

    @Autowired
    @Qualifier("gandiMailSendService")
    private IMailSendService mailSendService;
    @Value("${spring.profiles.active}")
    private String activeProfile;
    public ResponseMessage sendEmailUpcomingEvents(String templateCode) {
        ResponseMessage responseMessage ;
        String email = "ilyas_gulen@hotmail.com";
        String mailTitle = "Upcoming Events";

        Template template1 = tempService.findByCode(templateCode);//şablon kodu ile şablon çekiyoruz
        if(Objects.isNull(template1)||template1.getCode().isEmpty())
        {
            String mailBody=mailMessageProvider.getMessage("errorMailBody");
            responseMessage = mailSendService.sendMail(email, mailTitle, mailBody, new String[]{}, new String[]{});
            responseMessage.setMessage(mailBody);
            return responseMessage;
        }

        String mailBody = template1.getContent();
        Set<Event> events = adminMailService.getEvents();
        if(Objects.isNull(events)||events.isEmpty())
        {
            mailBody=mailMessageProvider.getMessage("errorEventBody");
            responseMessage = mailSendService.sendMail(email, mailTitle, mailBody, new String[]{}, new String[]{});
            responseMessage.setMessage(mailBody);
            return responseMessage;
        }
        String nextWeek =adminMailService.getNextWeek();   //hold next week
        String thisWeek =adminMailService.getThisWeek();   //hold this week
        mailBody = mailBody.replace(mailMessageProvider.getMessage("targetThisWeek"), thisWeek);   //event of this week
        mailBody = mailBody.replace(mailMessageProvider.getMessage("targetNextWeek"), nextWeek);    //event of next week
        String finalMailBody = mailBody.replace("{Name}","Nursel C.");
        responseMessage = mailSendService.sendMail(email, mailTitle, finalMailBody, new String[]{}, new String[]{});

        return responseMessage;
    }

}
