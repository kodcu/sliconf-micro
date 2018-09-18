package javaday.istanbul.sliconf.micro.security;



import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application-security.properties")
@ConfigurationProperties(prefix = "sliconf.security.authentication")
public  class TokenAuthenticationServiceProperties {

    private static String tokenSecret;

    public static String getTokenSecret() {
        return tokenSecret;
    }

    public static void setTokenSecret(String tokenSecret) {
        TokenAuthenticationServiceProperties.tokenSecret = tokenSecret;
    }
}
