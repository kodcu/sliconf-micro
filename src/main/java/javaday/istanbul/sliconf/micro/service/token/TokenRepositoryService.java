package javaday.istanbul.sliconf.micro.service.token;

import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.model.token.Token;
import javaday.istanbul.sliconf.micro.model.token.TokenType;
import javaday.istanbul.sliconf.micro.repository.TokenRepository;
import javaday.istanbul.sliconf.micro.util.Constants;
import javaday.istanbul.sliconf.micro.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class TokenRepositoryService implements TokenService {


    @Autowired
    private TokenRepository repo;


    /**
     * Gelen {@link Token}'in gecerli olup olmadigi kontrol eder buna gore {@link ResponseMessage} dondurur
     *
     * @param tokenValue
     * @return ResponseMessage
     */
    public ResponseMessage isTokenValid(String tokenValue) {
        ResponseMessage responseMessage = new ResponseMessage(false, "Token Is Not Valid", null);

        Token token = findTokenByTokenValueEquals(tokenValue);

        if (Objects.nonNull(token) && isTokenDateAfterNow(token) && Objects.nonNull(token.getValidMail())) {
            responseMessage.setStatus(true);
            responseMessage.setMessage("Token is Valid");
            responseMessage.setReturnObject(token.getValidMail());
        }

        return responseMessage;
    }

    @Override
    public Token findTokenByTokenValueEquals(String tokenValue) {
        return repo.findTokenByTokenValueEquals(tokenValue);
    }

    @Override
    public boolean save(Token token) {
        return Objects.nonNull(repo.save(token));
    }

    public void remove(Token token) {
        repo.delete(token);
    }

    /**
     * Token sure olarak hala gecerli mi kontrolu
     *
     * @param token
     * @return
     */
    private boolean isTokenDateAfterNow(Token token) {
        LocalDateTime now = LocalDateTime.now();
        return DateUtil.isFirstDateTimeBeforeOrEqualSecondDateTime(now, token.getValidUntilDate());
    }

    public Token generateToken(String email) {
        Token token = new Token();

        token.setValidMail(email);

        UUID uuid = UUID.randomUUID();

        token.setTokenValue(uuid.toString());

        token.setValidUntilDate(LocalDateTime.now(ZoneId.of("Asia/Istanbul")).plusMinutes(Constants.PASS_RESET_TIME_TO_LIVE_MINUTE));

        token.setType(TokenType.PASSWORD_RESET.toString());

        return token;
    }

    public List<Token> findAll() {
        return repo.findAll();
    }
}