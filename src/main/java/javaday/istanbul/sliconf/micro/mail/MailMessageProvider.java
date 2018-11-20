package javaday.istanbul.sliconf.micro.mail;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * Created by ttayfur on 15/10/17.
 */
@Component
public class MailMessageProvider {

    @Resource(name = "mailMessageProperties")
    private Properties properties;

    public Properties getProp() {
        return properties;
    }

    /**
     * mailMessage.properties dosyasinda bulunan mesajlara ulasmamizi saglayan metot
     *
     * @param key anahtar deger
     * @return mesaj
     */
    public String getMessage(String key) {
        return properties.getProperty(key);
    }
}
