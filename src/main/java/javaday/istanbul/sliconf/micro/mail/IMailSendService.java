package javaday.istanbul.sliconf.micro.mail;

import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
public interface IMailSendService {
    ResponseMessage sendMail(String email, String subject, String message, String[] cc, String[] bcc);
    ResponseMessage sendCompleteEventStateMail(Event event, String templateCode);
}
