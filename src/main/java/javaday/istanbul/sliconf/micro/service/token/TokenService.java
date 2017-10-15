package javaday.istanbul.sliconf.micro.service.token;

import javaday.istanbul.sliconf.micro.model.token.Token;

public interface TokenService {
    Token findTokenByTokenValueEquals(String token);

    boolean save(Token token);
}
