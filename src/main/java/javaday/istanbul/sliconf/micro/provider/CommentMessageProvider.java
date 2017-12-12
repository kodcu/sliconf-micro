package javaday.istanbul.sliconf.micro.provider;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * Created by ttayfur on 11/12/17.
 */
@Component
public class CommentMessageProvider {

    @Resource(name = "commentProperties")
    private Properties properties;

    public Properties getProp() {
        return properties;
    }

    /**
     * comment.properties dosyasinda bulunan mesajlara ulasmamizi saglayan metot
     *
     * @param key anahtar deger
     * @return mesaj
     */
    public String getMessage(String key) {
        return properties.getProperty(key);
    }
}
