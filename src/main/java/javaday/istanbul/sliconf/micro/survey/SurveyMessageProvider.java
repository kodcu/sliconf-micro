package javaday.istanbul.sliconf.micro.survey;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Properties;

@Component
public class SurveyMessageProvider {

    @Resource(name = "surveyProperties")
    private Properties properties;

    public Properties getProp() {
        return properties;
    }

    /**
     * survey.properties dosyasinda bulunan mesajlara ulasmamizi saglayan metot
     *
     * @param key anahtar deger
     * @return mesaj
     */
    public String getMessage(String key) {
        return properties.getProperty(key);
    }
}
