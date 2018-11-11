package javaday.istanbul.sliconf.micro.security.token;

public interface TokenService {
    Token findTokenByTokenValueEquals(String token);

    boolean save(Token token);
}
