package javaday.istanbul.sliconf.micro.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by ttayfur on 7/6/17.
 */
public class UserService {
    private Logger logger = LogManager.getLogger(getClass());
    private PasswordEncryptionService encryptionService = new PasswordEncryptionService();

    public byte[] getSalt() {
        byte[] salt = null;

        try {
            salt = encryptionService.generateSalt();
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
        }

        return salt;
    }

    public byte[] getHashedPassword(String password, byte[] salt) {
        byte[] ePass = null;

        try {
            ePass = encryptionService.getEncryptedPassword(password, salt);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error(e.getMessage(), e);
        }
        return ePass;
    }

    public boolean checkPassword(String password, byte[] hashedPassword, byte[] salt) {
        try {
            return encryptionService.authenticate(password, hashedPassword, salt);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }
}
