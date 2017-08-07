package javaday.istanbul.sliconf.micro.provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ttayfur on 8/7/17.
 */
public class EventControllerMessageProvider {
    private static EventControllerMessageProvider instance = null;

    private static final Object lock = new Object();

    private Properties properties = new Properties();

    private Logger logger;

    private EventControllerMessageProvider() {
        logger = LogManager.getLogger(EventControllerMessageProvider.class);
        InputStream inputStream;
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream("eventController.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static EventControllerMessageProvider instance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new EventControllerMessageProvider();
                }
            }
        }
        return instance;
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
