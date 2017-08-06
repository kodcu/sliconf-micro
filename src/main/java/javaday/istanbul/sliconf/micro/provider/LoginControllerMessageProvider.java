package javaday.istanbul.sliconf.micro.provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ttayfur on 7/29/17.
 */
public class LoginControllerMessageProvider {
    private static LoginControllerMessageProvider instance = null;

    private static final Object lock = new Object();

    private Properties properties = new Properties();

    private Logger logger;

    private LoginControllerMessageProvider() {
        logger = LogManager.getLogger(LoginControllerMessageProvider.class);
        InputStream inputStream;
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream("loginController.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static LoginControllerMessageProvider instance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new LoginControllerMessageProvider();
                }
            }
        }
        return instance;
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
