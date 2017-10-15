package javaday.istanbul.sliconf.micro.service.mail;

import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;

public interface IMailSendService {
    ResponseMessage sendMail(String email, String subject, String message, String[] cc, String[] bcc);
}
