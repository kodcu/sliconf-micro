package javaday.istanbul.sliconf.micro.user.service;


import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.security.token.Token;
import javaday.istanbul.sliconf.micro.mail.MailMessageProvider;
import javaday.istanbul.sliconf.micro.mail.IMailSendService;
import javaday.istanbul.sliconf.micro.security.token.TokenRepositoryService;
import javaday.istanbul.sliconf.micro.util.Constants;
import javaday.istanbul.sliconf.micro.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PasswordResetService {

    @Autowired
    @Qualifier("gandiMailSendService")
    private IMailSendService mailSendService;

    @Autowired
    private TokenRepositoryService tokenRepositoryService;

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Autowired
    private MailMessageProvider mailMessageProvider;

    /**
     * Girilen email gecerli bir email mi onu kontrol eder
     *
     * @param email
     * @return
     */
    public boolean checkIsMailValid(String email) {

        return EmailUtil.validateEmail(email);
    }

    public ResponseMessage sendPasswordResetMail(String email) {
        ResponseMessage responseMessage = new ResponseMessage(false, "message", "");

        if (checkIsMailValid(email)) {
            User user = userRepositoryService.findByEmail(email);

            if (Objects.nonNull(user)) {
                ResponseMessage generateResponseMessage = generateAndSaveNewToken(email);

                if (generateResponseMessage.isStatus()) {

                    String tokenValue = ((Token) generateResponseMessage.getReturnObject()).getTokenValue();

                    responseMessage = mailSendService.sendMail(email, "Password Reset",
                            String.format(
                                    mailMessageProvider.getMessage("mailPassResetMessage"),
                                    "http://app.sliconf.com/resetPass/" + tokenValue,
                                    Constants.PASS_RESET_TIME_TO_LIVE_MINUTE),
                            new String[]{}, new String[]{});
                } else {
                    return generateResponseMessage;
                }
            } else {
                responseMessage.setMessage("Email can not found!");
            }

        } else {
            responseMessage.setStatus(false);
            responseMessage.setMessage(mailMessageProvider.getMessage("mailNotValid"));
        }

        return responseMessage;
    }

    /**
     * Yeni bir token oluşturur ve dbye kaydeder
     *
     * @param email
     * @return
     */
    private ResponseMessage generateAndSaveNewToken(String email) {
        ResponseMessage responseMessage = new ResponseMessage(false, "Token can not generated!", "");

        Token token = tokenRepositoryService.generateToken(email);

        if (Objects.isNull(token)) {
            return responseMessage;
        }

        if (tokenRepositoryService.save(token)) {
            responseMessage.setStatus(true);
            responseMessage.setMessage("Token successfully generated and saved");
            responseMessage.setReturnObject(token);
        }

        return responseMessage;
    }
}
