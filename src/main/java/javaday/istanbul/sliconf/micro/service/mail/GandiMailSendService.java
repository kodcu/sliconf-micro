package javaday.istanbul.sliconf.micro.service.mail;

import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.MailMessageProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service("gandiMailSendService")
public class GandiMailSendService implements IMailSendService {

    private static final String username = "info@sliconf.com";
    private static final String password = "eddieHendorsoN!91";

    private static final String from = "info@sliconf.com";

    private final Logger logger = LoggerFactory.getLogger(GandiMailSendService.class);

    public static boolean isTest = false;

    @Autowired
    private MailMessageProvider mailMessageProvider;

    @Override
    public ResponseMessage sendMail(String email, String subject, String text, String[] cc, String[] bcc) {

        ResponseMessage responseMessage = new ResponseMessage(false,
                mailMessageProvider.getMessage("mailCanNotSent"), "");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "mail.gandi.net");
        props.put("mail.smtp.port", "587");

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

            if (!isTest) {
                Transport.send(message);
            }

            responseMessage.setStatus(true);
            responseMessage.setMessage(mailMessageProvider.getMessage("mailSent"));

            logger.info(mailMessageProvider.getMessage("mailSentFromTo"), from, email);

        } catch (MessagingException e) {
            logger.error(e.getMessage(), e);
        }

        return responseMessage;
    }
}
