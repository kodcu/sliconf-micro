package javaday.istanbul.sliconf.micro.admin;
import javaday.istanbul.sliconf.micro.template.Service.TemplateRepositoryService;
import javaday.istanbul.sliconf.micro.template.model.Template;
import javaday.istanbul.sliconf.micro.mail.IMailSendService;
import javaday.istanbul.sliconf.micro.mail.MailMessageProvider;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javaday.istanbul.sliconf.micro.event.model.Event;
import java.util.*;
@Slf4j
@RequiredArgsConstructor
@Component
@Profile({"prod", "dev"})
public class EventStateSendMail {
    private final TemplateRepositoryService tempService;
    private final AdminMailService adminMailService;
    private final MailMessageProvider mailMessageProvider;

    @Autowired
    @Qualifier("gandiMailSendService")
    private IMailSendService mailSendService;
    @Scheduled(cron = "0 30 15 ? * TUE",zone="Europe/Istanbul")
    public ResponseMessage sendEmailUpcomingEvents() {

        ResponseMessage responseMessage ;
        String email = mailMessageProvider.getMessage("email"); //emaili source dan alıyoruz
        String mailTitle = mailMessageProvider.getMessage("mailTitle");//mail title source dan  alıyoruz
        log.info("email and title received");

        Template template = tempService.findByCode("textMail3");//şablon kodu ile şablon çekiyoruz
        if(Objects.isNull(template))
        {
            String mailBody=mailMessageProvider.getMessage("errorMailBody");
            responseMessage = mailSendService.sendMail(email, mailTitle, mailBody, new String[]{}, new String[]{});
            responseMessage.setMessage(mailBody);
            return responseMessage;
        }
        log.info("mail template received");

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
       log.info("mail send to admin");

       return responseMessage;
    }

}