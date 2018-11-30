package javaday.istanbul.sliconf.micro.admin;
import javaday.istanbul.sliconf.micro.template.Service.TemplateRepositoryService;
import javaday.istanbul.sliconf.micro.template.model.Template;
import javaday.istanbul.sliconf.micro.mail.IMailSendService;
import javaday.istanbul.sliconf.micro.mail.MailMessageProvider;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javaday.istanbul.sliconf.micro.event.model.Event;
import java.util.*;
@RequiredArgsConstructor
@Component
public class EventStateSendMail {
    private final TemplateRepositoryService tempService;
    private final AdminMailService adminMailService;
    private final MailMessageProvider mailMessageProvider;

    @Value("${spring.profiles.active}")
    private String activeProfile;
    @Autowired
    @Qualifier("gandiMailSendService")
    private IMailSendService mailSendService;
    @Scheduled(cron = "0 00  10 ? * MON")
    public ResponseMessage sendEmailUpcomingEvents() {

        ResponseMessage responseMessage ;
        String email = mailMessageProvider.getMessage("email"); //emaili source dan alıyoruz
        String mailTitle = mailMessageProvider.getMessage("mailTitle");//mail title source dan  alıyoruz

        Template template = tempService.findByCode("textMail3");//şablon kodu ile şablon çekiyoruz
        if(Objects.isNull(template))
        {
            String mailBody=mailMessageProvider.getMessage("errorMailBody");
            responseMessage = mailSendService.sendMail(email, mailTitle, mailBody, new String[]{}, new String[]{});
            responseMessage.setMessage(mailBody);
            return responseMessage;
        }

        String mailBody = template.getContent();
        Set<Event> events = adminMailService.getEvents();
        if(Objects.isNull(events))
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