package javaday.istanbul.sliconf.micro.steps.admin;

import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.event.EventBuilder;
import javaday.istanbul.sliconf.micro.event.controller.CreateEventRoute;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.mail.IMailSendService;
import javaday.istanbul.sliconf.micro.template.Service.TemplateRepositoryService;
import javaday.istanbul.sliconf.micro.template.model.Template;
import javaday.istanbul.sliconf.micro.admin.EventStateSendMailTest;
import javaday.istanbul.sliconf.micro.mail.MailMessageProvider;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import lombok.RequiredArgsConstructor;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RequiredArgsConstructor
@Ignore
public class AdminSendMailTest extends SpringBootTestConfig {

    private final   TemplateRepositoryService templateRepositoryService;
    private ResponseMessage responseMessage;
    private String code;
    private String email;
    private String sub;
    private final MailMessageProvider    mailMessageProvider;
    private final EventStateSendMailTest  eventStateSendMailTest;
    private final CreateEventRoute createEventRoute;
    private final UserRepositoryService userRepositoryService;
    private final TemplateRepositoryService tempService;
    private Event event1;
    @Autowired
    @Qualifier("gandiMailSendService")
    private IMailSendService mailSendService;

    @Diyelimki("^Yeni bir sablon olusturuluyor$")
    public void yeniSablon()throws Throwable{
        Template template=new Template();
        Template template1=new Template();
        Template template2=new Template();
        Template template3=new Template();
        template.setTitle("textMail");
        template1.setTitle("teffe");
        template2.setTitle("erfe");
        template3.setTitle("sdsds");
        String x="<html>\n" +
                "<body>\n" +
                "<p>&nbsp;</p>\n" +
                "<div style=\"width: 100%;\">\n" +
                "<table style=\"margin: 0px auto; width: 428px; height: 723px;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "<tbody>\n" +
                "<tr style=\"height: 21px;\">\n" +
                "<td style=\"background-color: #29b573; width: 440px; height: 21px;\">&nbsp;</td>\n" +
                "</tr>\n" +
                "<tr style=\"height: 685px;\">\n" +
                "<td style=\"border-radius: 4px; padding: 20px; width: 400px; height: 710px;\" rowspan=\"2\"><a style=\"border: none;\" href=\"https://sliconf.com\" target=\"_blank\" rel=\"noopener noreferrer\" data-auth=\"NotApplicable\"><img style=\"border: none;\" src=\"https://sliconf.com/wp-content/uploads/2018/08/logo.jpg\" width=\"130\" data-imagetype=\"External\" /> </a>\n" +
                "<p>Sliconf is here with the events of the week.Below you can see what's happening this week and next week's digital world events.See you next week.</p>\n" +
                "<p>\n" +
                "<p><b>Events of this week </b></p>\n" +
                "<ul>\n" +
                "{Events of this week}\n" +
                "</ul>\n" +
                "<p><b>Events of next week</b></p>\n" +
                "<ul>\n" +
                "{Events of next week}\n" +
                "</ul>\n" +
                "<p>Sliconf Team</p>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td style=\"background: #f7f7f7;\" width=\"480\" height=\"25\">&nbsp;</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td style=\"background-color: #29b573; width: 440px; height: 21px;\" width=\"540\" height=\"13\">&nbsp;</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
        String x1="`<div style=\\\"width:100%\\\">\\n\" +\n" +
                "                \"<table width=\\\"540\\\" border=\\\"0\\\" cellpadding=\\\"0\\\" cellspacing=\\\"0\\\" style=\\\"margin:0 auto\\\">\\n\" +\n" +
                "                \"<tbody>\\n\" +\n" +
                "                \"<tr>\\n\" +\n" +
                "                \"<td colspan=\\\"3\\\" width=\\\"540\\\" height=\\\"21\\\" style=\\\"background:url(https://etkinlik.webrazzi.com/assets/img/email/layout_03.jpg) #f7f7f7 no-repeat\\\">\\n\" +\n" +
                "                \"</td>\\n\" +\n" +
                "                \"</tr>\\n\" +\n" +
                "                \"<tr>\\n\" +\n" +
                "                \"<td rowspan=\\\"2\\\" width=\\\"30\\\" height=\\\"65\\\" style=\\\"background:url(https://etkinlik.webrazzi.com/assets/img/email/layout_05.jpg) #f7f7f7 no-repeat\\\">\\n\" +\n" +
                "                \"</td>\\n\" +\n" +

                "                \"</div>`";
        String x2="`<div style=\\\"width:100%\\\">\\n\" +\n" +
                "                \"<table width=\\\"540\\\" border=\\\"0\\\" cellpadding=\\\"0\\\" cellspacing=\\\"0\\\" style=\\\"margin:0 auto\\\">\\n\" +\n" +
                "                \"<tbody>\\n\" +\n" +
                "                \"<tr>\\n\" +\n" +
                "                \"<td colspan=\\\"3\\\" width=\\\"540\\\" height=\\\"21\\\" style=\\\"background:url(https://etkinlik.webrazzi.com/assets/img/email/layout_03.jpg) #f7f7f7 no-repeat\\\">\\n\" +\n" +
                "                \"</td>\\n\" +\n" +
                "                \"</tr>\\n\" +\n" +
                "                \"<tr>\\n\" +\n" +
                "                \"<td rowspan=\\\"2\\\" width=\\\"30\\\" height=\\\"65\\\" style=\\\"background:url(https://etkinlik.webrazzi.com/assets/img/email/layout_05.jpg) #f7f7f7 no-repeat\\\">\\n\" +\n" +
                "                \"</td>\\n\" +\n" +
                "<p>Sliconf is here with the events of the week.Below you can see what's happening this week and next week's digital world events.See you next week.  </p>\n" +
                "<p><b>Events of this week </b></p>\n" +

                "                \"</div>`";
        String x3=" \"<p>Sliconf is here with the events of the week.Below you can see what's happening this week and next week's digital world events.See you next week.  </p>\\n\" +\n" +
                "                \"<p><b>Events of this week </b></p>\\n\" +";
        String x4="<div style=\"width:100%\">\n" +
                "<table width=\"540\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin:0 auto\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td colspan=\"3\" width=\"540\" height=\"21\" style=\"background:url(https://image.ibb.co/iSWEZf/layout-03.jpg) #f7f7f7 no-repeat\">\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td rowspan=\"2\" width=\"30\" height=\"65\" style=\"background:url(https://image.ibb.co/hwUF70/layout-05.png) #f7f7f7 no-repeat\">\n" +
                "</td>\n" +
                "<td width=\"480\" height=\"1\" style=\"background:url(https://image.ibb.co/jHhk70/layout-06.png) #f7f7f7 no-repeat\">\n" +
                "</td>\n" +
                "<td rowspan=\"2\" width=\"30\" height=\"65\" style=\"background:url(https://image.ibb.co/caH2n0/layout-07.png) #f7f7f7 no-repeat\">\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td rowspan=\"2\" width=\"440\" style=\"border-radius:4px; padding:20px\"><a href=\"https://sliconf.com\" target=\"_blank\" rel=\"noopener noreferrer\" data-auth=\"NotApplicable\" style=\"border:none\"><img data-imagetype=\"External\" src=\"https://sliconf.com/wp-content/uploads/2018/08/logo.png\" width=\"130\" style=\"border:none\"> </a>\n" +
                "<p>Hi Nursel C.,</p>\n" +
                "<p>There is a new event</p>\n" +
                "<p><b>New Complete Event! </b></p>\n" +
                "{Event}</ul>\n" +
                "Sliconf Team </p>\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td rowspan=\"2\" width=\"30\" style=\"background:#f7f7f7\">&nbsp;</td>\n" +
                "<td rowspan=\"2\" width=\"30\" style=\"background:#f7f7f7\">&nbsp;</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td width=\"480\" height=\"25\" style=\"background:#f7f7f7\">&nbsp;</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td colspan=\"3\" width=\"540\" height=\"13\" style=\"background:url(https://image.ibb.co/m7M6Ef/layout-12.png) #f7f7f7 no-repeat\">\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</div>";
        String x5="<html>\n" +
                "<body>\n" +
                "<p>&nbsp;</p>\n" +
                "<div style=\"width: 100%;\">\n" +
                "<table style=\"margin: 0px auto; width: 428px; height: 723px;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "<tbody>\n" +
                "<tr style=\"height: 21px;\">\n" +
                "<td style=\"background-color: #29b573; width: 440px; height: 21px;\">&nbsp;</td>\n" +
                "</tr>\n" +
                "<tr style=\"height: 685px;\">\n" +
                "<td style=\"border-radius: 4px; padding: 20px; width: 400px; height: 710px;\" rowspan=\"2\"><a style=\"border: none;\" href=\"https://sliconf.com\" target=\"_blank\" rel=\"noopener noreferrer\" data-auth=\"NotApplicable\"><img style=\"border: none;\" src=\"https://sliconf.com/wp-content/uploads/2018/08/logo.png\" width=\"130\" data-imagetype=\"External\" /> </a>\n" +
                "<p>There is a new event.</p>\n" +
                "<table style=\"width: 400px;\" border=\"0\" cellspacing=\"1\" cellpadding=\"1\">\n" +
                "<tbody>\n" +
                "<tr style=\"height: 46px;\">\n" +
                "<td style=\"height: 46px; width: 392px;\">\n" +
                "<p>Event Name:{Event}</p>\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr style=\"height: 46px;\">\n" +
                "<td style=\"height: 46px; width: 392px;\">\n" +
                "<p>Event Web Page:{EventWeb}&nbsp;</p>\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr style=\"height: 46px;\">\n" +
                "<td style=\"height: 46px; width: 392px;\">\n" +
                "<p>Event Start Date:{ESD}</p>\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr style=\"height: 45px;\">\n" +
                "<td style=\"height: 45px; width: 392px;\">\n" +
                "<p>Event Finish Date:{EFD}</p>\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr style=\"height: 40px;\">\n" +
                "<td style=\"height: 40px; width: 392px;\">\n" +
                "<p>User Email:{UserMail}</p>\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr style=\"height: 51px;\">\n" +
                "<td style=\"height: 51px; width: 392px;\">\n" +
                "<p>Package Type :{PackageType}</p>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>Sliconf Team</p>\n" +
                "<p>&nbsp;</p>\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr style=\"height: 25px;\">\n" +
                "<td style=\"background: #f7f7f7; width: 5px; height: 25px;\">&nbsp;</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td style=\"background-color: #29b573; width: 440px;\">&nbsp;</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
        Template template7 =new Template();
        Template template10=new Template();
        template1.setContent(x1);
        template2.setContent(x2);
        template3.setContent(x3);
        template7.setContent(x4);
        template10.setContent(x5);
        //everything is ok
        template.setContent(x);
        template.setCode("textMail");
        template1.setCode("teefef");
        template2.setCode("sdedef");
        template3.setCode("xssds");
        template7.setCode("rereererere");
        template7.setTitle("textMailComplete");
        template10.setCode("textMailComplete");
        template10.setTitle("textMail5");

        ResponseMessage savedTemplateMessage=  templateRepositoryService.save(template);
        ResponseMessage savedTemplateMessage2= templateRepositoryService.save(template1);
        ResponseMessage savedTemplateMessage3= templateRepositoryService.save(template2);
        ResponseMessage savedTemplateMessage4= templateRepositoryService.save(template3);
        ResponseMessage savedTemplateMessage8=templateRepositoryService.save(template7);
        ResponseMessage savedTemplateMessage9=templateRepositoryService.save(template10);
        assertTrue(savedTemplateMessage.isStatus());
        assertTrue(savedTemplateMessage2.isStatus());
        assertTrue(savedTemplateMessage3.isStatus());
        assertTrue(savedTemplateMessage4.isStatus());
        assertTrue(savedTemplateMessage8.isStatus());
        assertTrue(savedTemplateMessage9.isStatus());
        //if  code null
        Template template4=new Template();
        template4.setCode(null);
        template4.setTitle("dsd");
        template4.setContent("");
        ResponseMessage savedTemplateMessage5= templateRepositoryService.save(template4);
        assertFalse(savedTemplateMessage5.isStatus());
        ////if  code is empty
        Template template5=new Template();
        template5.setCode("");
        template5.setTitle("");
        template5.setContent("");
        ResponseMessage savedTemplateMessage6= templateRepositoryService.save(template5);
        assertFalse(savedTemplateMessage6.isStatus());
        //if  code already used
        Template template6=new Template();
        template6.setCode("textMail");
        template6.setContent("");
        template6.setTitle("");
        ResponseMessage savedTemplateMessage7=templateRepositoryService.save(template6);
        assertFalse(savedTemplateMessage7.isStatus()); 
    }
    @Diyelimki("^Basarili mail gonderiyoruz$")
    public void mailGonderiyoruz()throws Throwable{

         email="ilyas_gulen@hotmail.com";
         sub="upcoming events";
    }
    @Eğerki("^Template null degilse$")
    public void templateNullDegilse() throws Throwable {

         code="textMail";

    }
    @Ozaman("^Mail basarili sekilde gonderilir$")
    public void mailBasariliSekildeGonderilir()throws Throwable{
        responseMessage=eventStateSendMailTest.sendEmailUpcomingEvents(code);
        assertTrue(responseMessage.isStatus());
    }
    @Diyelimki("^Bos mail mesaji gonderiyoruz$")
    public void bosMailMesajiGonderiyoruz()throws Throwable{

        email="ilyas_gulen@hotmail.com";
        sub="upcoming events";
    }
    @Eğerki("^template null ise$")
    public void templateNullise() throws Throwable {
        code="textsdsdsdsMail";

    }
    @Ozaman("^Admine template bos mesaji  gonderilir$")
    public void templateBosMesajiGonderilir()throws Throwable{
        responseMessage=eventStateSendMailTest.sendEmailUpcomingEvents(code);
        String message=mailMessageProvider.getMessage("errorMailBody");
        assertEquals(responseMessage.getMessage(),message);
    }
    @Diyelimki("^event basarili sekide kaydedildi$")
    public void eventKaydedildi() throws Throwable{
        User user1 = new User();
        user1.setUsername("createEventUser1061");
        user1.setEmail("createEventUser1061@sliconf.com");
        user1.setPassword("123123123123");
        ResponseMessage savedUserMessage1 = userRepositoryService.saveUser(user1);
        assertTrue(savedUserMessage1.isStatus());
        String userId1 = ((User) savedUserMessage1.getReturnObject()).getId();
        // Everything is ok
        event1 = new EventBuilder()
                .setName("Create Event 8061")
                .setExecutiveUser(userId1)
                .setDate(LocalDateTime.now().plusWeeks(2))
                .setEndDate(LocalDateTime.now().plusWeeks(3))
                .build();
        ResponseMessage createEventMessage1 = createEventRoute.processEvent(event1, userId1);
        assertTrue(createEventMessage1.isStatus());
    }
    @Eğerki("^template null degilse$")
    public void NullDegilse() throws Throwable{
        code="textMailComplete";
        Template template=tempService.findByCode(code);
        assertNotNull(template);
    }
    @Ozaman("^Admine kaydedilen event mail gonderilir$")
    public void eventMailGonderilir()throws Throwable{
        responseMessage = mailSendService.sendCompleteEventStateMail(event1,code);
        assertTrue(responseMessage.isStatus());
    }
    @Eğerki("^Template null ise$")
    public void templateNullsa()throws Throwable{
        code="fdffdvdfddvd";
        Template template=tempService.findByCode(code);
        assertNull(template);
    }
    @Ozaman("^Admine template null maili gonderlir$")
    public void templateNullMailiGonderilir()throws Throwable{
        responseMessage=mailSendService.sendCompleteEventStateMail(event1,code);
        String message=mailMessageProvider.getMessage("errorMailBody");
        assertEquals(message,responseMessage.getMessage());
    }
    @Diyelimki("^event basarili sekide kaydedilmedi$")
    public void eventKaydedilmedi()throws Throwable{
        User user2 = new User();
        user2.setUsername("createEventUser86");
        user2.setEmail("createEventUser86@sliconf.com");
        user2.setPassword("123123123");
        ResponseMessage savedUserMessage2 = userRepositoryService.saveUser(user2);

        assertTrue(savedUserMessage2.isStatus());
        String userId2 = ((User) savedUserMessage2.getReturnObject()).getId();
        // invalid Date
        Event event2 = new EventBuilder()
                .setName("Create Event 87")
                .setExecutiveUser(userId2)
                .setDate(LocalDateTime.now().minusMonths(2)).build();
        responseMessage = createEventRoute.processEvent(event2, userId2);
        assertFalse(responseMessage.isStatus());
    }
    @Ozaman("^Admine mail gonderilmez$")
    public void mailGonderilmez()throws Throwable{
        responseMessage= mailSendService.sendCompleteEventStateMail(null,code);
        assertFalse(responseMessage.isStatus());
    }



}
