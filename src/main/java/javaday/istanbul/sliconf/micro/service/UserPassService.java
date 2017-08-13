package javaday.istanbul.sliconf.micro.service;

import javaday.istanbul.sliconf.micro.model.User;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by ttayfur on 7/6/17.
 */
public class UserPassService {
    //private Logger logger = LogManager.getLogger(getClass());
    private PasswordEncryptionService encryptionService = new PasswordEncryptionService();

    public byte[] getSalt() {
        byte[] salt = null;

        try {
            salt = encryptionService.generateSalt();
        } catch (NoSuchAlgorithmException e) {
            //logger.error(e.getMessage(), e);
        }

        return salt;
    }

    public byte[] getHashedPassword(String password, byte[] salt) {
        byte[] ePass = null;

        try {
            ePass = encryptionService.getEncryptedPassword(password, salt);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            //logger.error(e.getMessage(), e);
        }
        return ePass;
    }

    private boolean checkPassword(String password, byte[] hashedPassword, byte[] salt) {
        try {
            return encryptionService.authenticate(password, hashedPassword, salt);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            //logger.error(e.getMessage(), e);
            return false;
        }
    }

    public boolean checkIfUserAuthenticated(String password, byte[] hashedPassword, byte[] salt) {
        return checkPassword(password, hashedPassword, salt);
    }

    public User createNewUserWithHashedPassword(User user) {
        byte[] salt = getSalt();
        byte[] hashedPassword = getHashedPassword(user.getPassword(), salt);

        user.setSalt(salt);
        user.setHashedPassword(hashedPassword);

        user.setPassword("");
        return user;
    }
}
