package javaday.istanbul.sliconf.micro.mail;

import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.template.Service.TemplateRepositoryService;
import javaday.istanbul.sliconf.micro.template.model.Template;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

@Service("gandiMailSendService")
public class GandiMailSendService implements IMailSendService {

    private final Logger logger = LoggerFactory.getLogger(GandiMailSendService.class);


    @Value("${sliconf.mail.username}")
    private String username;

    @Value("${sliconf.mail.password}")
    private String password;

    @Value("${sliconf.mail.from}")
    private String from;

    @Value("${sliconf.mail.isTest}")
    private boolean isTest = false;

    @Value("${sliconf.mail.smtp.auth}")
    private String mailSmtpAuth;

    @Value("${sliconf.mail.smtp.host}")
    private String mailSmtpHost;

    @Value("${sliconf.mail.smtp.port}")
    private String mailSmtpPort;

    @Value("${sliconf.mail.smtp.starttls}")
    private String startTls;


    @Autowired
    private MailMessageProvider mailMessageProvider;
    @Autowired
    private  TemplateRepositoryService tempService;
    @Autowired
    private   UserRepositoryService userRepositoryService;

    @Override
    public ResponseMessage sendMail(String email, String subject, String text, String[] cc, String[] bcc) {

        ResponseMessage responseMessage = new ResponseMessage(false,
                mailMessageProvider.getMessage("mailCanNotSent"), "");

        Properties props = new Properties();
        props.put("mail.smtp.auth", mailSmtpAuth);
        props.put("mail.smtp.host", mailSmtpHost);
        props.put("mail.smtp.port", mailSmtpPort);
        props.put("mail.smtp.starttls.enable", startTls);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                }
        );

        try {

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));

            message.setSubject(subject);
            message.setText(text);
            message.setContent(text, "text/html; charset=utf-8");

            if (!isTest) {
                Transport.send(message);
            }

            responseMessage.setStatus(true);
            responseMessage.setMessage(mailMessageProvider.getMessage("mailSent"));

            String logMessage = String.format(mailMessageProvider.getMessage("mailSentFromTo"), from, email);

            logger.info(logMessage);

        } catch (MessagingException e) {
            logger.error(e.getMessage(), e);
        }

        return responseMessage;
    }
    /**
     * ADMİNE COPMLETE OLAN YADA UPDATE EDİLEN EVENTLERİ MAİL ATAN FONKSİYON
     * @param event
     * @param templateCode
     * @return
     */
    public ResponseMessage sendCompleteEventStateMail(Event event, String templateCode){
        ResponseMessage responseMessage;
        User user =new User();
        Optional<User> userOptional=userRepositoryService.findById(event.getExecutiveUser());
        userOptional.ifPresent(user1 -> {
            user.setRole(user1.getRole());
            user.setUsername(user1.getUsername());
            user.setEmail(user1.getEmail());
            user.setFullName(user1.getFullName());

        });
        if(Objects.isNull(event))
        {
            return new ResponseMessage(false, "mail can not send",null);
        }
        String   email = mailMessageProvider.getMessage("email"); //emaili source dan alıyoruz
        String   mailTitle = "New Complete Event";
        Template template = tempService.findByCode(templateCode); //template kodu ile template çekiyoruz
        if (Objects.isNull(template)||template.getCode().isEmpty())
        {
            responseMessage=sendMail(email,mailTitle,mailMessageProvider.getMessage("errorMailBody"),new String[]{}, new String[]{});
            responseMessage.setMessage(mailMessageProvider.getMessage("errorMailBody"));// veri tabanından cektiğimiz template nullsa admine template silindi mesajı gider
            return responseMessage;
        }
        String mailbody=template.getContent();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String eventStartDate=event.getStartDate().format(formatter);
        String eventFinishDate=event.getEndDate().format(formatter);
        String eventPackage=event.getEventState().getType();
        String eventWebSite=event.getAbout().getWeb();
        mailbody=mailbody.replace("{Event}",event.getName()); //cektiğimiz template de  {Event} yazan yer ile event ismi ile degistiriyoruz.
        mailbody=mailbody.replace("{ESD}",eventStartDate);
        mailbody=mailbody.replace("{EFD}",eventFinishDate);
        mailbody=mailbody.replace("{EventWeb}",eventWebSite);
        if(Objects.nonNull(eventPackage))
           mailbody=mailbody.replace("{PackageType}",eventPackage);
        else
            mailbody=mailbody.replace("{PackageType}","");
        mailbody=mailbody.replace("{UserMail}",user.getEmail());


        responseMessage=sendMail(email,mailTitle,mailbody,new String[]{},new String[]{});

        return responseMessage;

    }
}
