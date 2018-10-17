package javaday.istanbul.sliconf.micro.security;



import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
/**
 * {@link TokenAuthenticationService}'in çalışabilmesi için
 * application-security.properties isimli bir propery oluşturup içine
 * sliconf.security.authentication.tokenSecret= "secret"
 * isimli bir değer tanımlanmalıdır.
 * Buradaki "secret" tokenların şifrelenmesi ve çözülmesinde kullanılacak olan
 * değerdir.
 *
 */

@Component
@PropertySource("classpath:application-security.properties")
@ConfigurationProperties(prefix = "sliconf.security.authentication")
public  class TokenAuthenticationServiceProperties {

    private TokenAuthenticationServiceProperties() {}

    private static String tokenSecret;

    static String getTokenSecret() {
        return tokenSecret;
    }

    public static void setTokenSecret(String tokenSecret) {
        TokenAuthenticationServiceProperties.tokenSecret = tokenSecret;
    }
}
