package javaday.istanbul.sliconf.micro.provider;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * Created by ttayfur on 15/12/17.
 */
@Component
public class VoteMessageProvider {

    @Resource(name = "voteProperties")
    private Properties properties;

    public Properties getProp() {
        return properties;
    }

    /**
     * vote.properties dosyasinda bulunan mesajlara ulasmamizi saglayan metot
     *
     * @param key anahtar deger
     * @return mesaj
     */
    public String getMessage(String key) {
        return properties.getProperty(key);
    }
}
