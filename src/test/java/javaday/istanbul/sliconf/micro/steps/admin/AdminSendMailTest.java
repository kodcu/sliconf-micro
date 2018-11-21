package javaday.istanbul.sliconf.micro.steps.admin;

import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.Template.Service.TemplateRepositoryService;
import javaday.istanbul.sliconf.micro.Template.Template;
import javaday.istanbul.sliconf.micro.admin.EventStateSendMailTest;
import javaday.istanbul.sliconf.micro.mail.MailMessageProvider;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.junit.Ignore;
import static org.junit.Assert.*;

@RequiredArgsConstructor
@Ignore
public class AdminSendMailTest extends SpringBootTestConfig {

    private final   TemplateRepositoryService templateRepositoryService;
    private String email;
    private String sub;
    private ResponseMessage responseMessage;
    private String code;
    private final MailMessageProvider    mailMessageProvider;
    private final EventStateSendMailTest  eventStateSendMailTest;

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
        String x="<div style=\"width:100%\">\n" +
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
                "<p>Merhaba {Name},</p>\n" +
                "<p>Sliconf is here with the events of the week.Below you can see what's happening this week and next week's digital world events.See you next week.  </p>\n" +
                "<p><b>Events of this week </b></p>\n" +

                "{Events of this week}</ul>\n" +
                "<p><b>Events of next week</b></p>\n" +

                "{Events of next week}</ul>\n" +

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
        template1.setContent(x1);
        template2.setContent(x2);
        template3.setContent(x3);
        //everything is ok
        template.setContent(x);
        template.setCode("textMail");
        template1.setCode("teefef");
        template2.setCode("sdedef");
        template3.setCode("xssds");

        ResponseMessage savedTemplateMessage=  templateRepositoryService.save(template);
        ResponseMessage savedTemplateMessage2= templateRepositoryService.save(template1);
        ResponseMessage savedTemplateMessage3= templateRepositoryService.save(template2);
        ResponseMessage savedTemplateMessage4= templateRepositoryService.save(template3);
        assertTrue(savedTemplateMessage.isStatus());
        assertTrue(savedTemplateMessage2.isStatus());
        assertTrue(savedTemplateMessage3.isStatus());
        assertTrue(savedTemplateMessage4.isStatus());
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
        responseMessage=eventStateSendMailTest.sendEmailUpcomingEvents(email,sub,code);
        assertTrue(responseMessage.isStatus());
    }
    @Diyelimki("^Bos mail mesaji gonderiyoruz$")
    public void bosMailMesajiGonderiyoruz()throws Throwable{

        email="ilyas_gulen@hotmail.com";
        sub="upcoming events";
    }
    @Eğerki("^Template null ise$")
    public void templateNullsa() throws Throwable {
        code="textsdsdsdsMail";

    }
    @Ozaman("^Admine template bos mesaji  gonderilir$")
    public void templateBosMesajiGonderilir()throws Throwable{
        responseMessage=eventStateSendMailTest.sendEmailUpcomingEvents(email,sub,code);
        String message=mailMessageProvider.getMessage("errorMailBody");
        assertEquals(responseMessage.getMessage(),message);
    }



}
