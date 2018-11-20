package javaday.istanbul.sliconf.micro.user;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * Created by ttayfur on 8/7/17.
 */
@Component
public class LoginControllerMessageProvider {

    @Resource(name = "loginProperties")
    private Properties properties;

    public Properties getProp() {
        return properties;
    }

    /**
     * loginController.properties dosyasinda bulunan mesajlara ulasmamizi saglayan metot
     *
     * @param key anahtar deger
     * @return mesaj
     */
    public String getMessage(String key) {
        return properties.getProperty(key);
    }
}
