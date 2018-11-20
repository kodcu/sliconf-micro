package javaday.istanbul.sliconf.micro.mail;

import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
}
