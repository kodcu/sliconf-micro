package javaday.istanbul.sliconf.micro.event;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * Created by ttayfur on 8/7/17.
 */
@Component
public class EventControllerMessageProvider {

    @Resource(name = "eventProperties")
    private Properties properties;

    public Properties getProp() {
        return properties;
    }

    /**
     * eventController.properties dosyasinda bulunan mesajlara ulasmamizi saglayan metot
     *
     * @param key anahtar deger
     * @return mesaj
     */
    public String getMessage(String key) {
        return properties.getProperty(key);
    }
}
