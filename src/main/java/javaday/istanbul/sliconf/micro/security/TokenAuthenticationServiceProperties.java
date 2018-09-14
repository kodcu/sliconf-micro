package javaday.istanbul.sliconf.micro.security;



import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application-security.properties")
@ConfigurationProperties(prefix = "sliconf.security.authentication")
public  class TokenAuthenticationServiceProperties {

    private static String secret;

    public static String getSecret() {
        return secret;
    }

    public static void setSecret(String secret) {
        TokenAuthenticationServiceProperties.secret = secret;
    }
}
