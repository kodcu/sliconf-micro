package javaday.istanbul.sliconf.micro.security.token;

import javaday.istanbul.sliconf.micro.security.token.Token;

public interface TokenService {
    Token findTokenByTokenValueEquals(String token);

    boolean save(Token token);
}
