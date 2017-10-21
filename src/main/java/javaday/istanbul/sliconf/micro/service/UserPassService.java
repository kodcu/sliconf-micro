package javaday.istanbul.sliconf.micro.service;

import javaday.istanbul.sliconf.micro.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


/**
 * Created by ttayfur on 7/6/17.
 */
public class UserPassService {
    private Logger logger = LoggerFactory.getLogger(UserPassService.class);

    private PasswordEncryptionService encryptionService = new PasswordEncryptionService();

    private byte[] getSalt() {
        byte[] salt = null;

        try {
            salt = encryptionService.generateSalt();
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
        }

        return salt;
    }

    private byte[] getHashedPassword(String password, byte[] salt) {
        byte[] ePass = null;

        try {
            ePass = encryptionService.getEncryptedPassword(password, salt);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error(e.getMessage(), e);
        }
        return ePass;
    }

    public boolean checkPassword(String password, byte[] hashedPassword, byte[] salt) {
        boolean isOk = false;
        try {
            isOk = encryptionService.authenticate(password, hashedPassword, salt);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error(e.getMessage(), e);
        }

        return isOk;
    }

    /**
     *
     * @param sourceUser db den cekilmis salt bulunduran user
     * @param targetUser sifresi bulunan ve db user ile karsilastirilacak user
     * @return
     */
    public boolean checkIfUserAuthenticated(User sourceUser, User targetUser) {
        byte[] salt = sourceUser.getSalt();
        byte[] hashedPassword = sourceUser.getHashedPassword();

        return checkPassword(targetUser.getPassword(), hashedPassword, salt);
    }

    // TODO:Yorum satirlari eklenecek
    public User createNewUserWithHashedPassword(User user) {
        byte[] salt = getSalt();
        byte[] hashedPassword = getHashedPassword(user.getPassword(), salt);

        user.setSalt(salt);
        user.setHashedPassword(hashedPassword);

        user.setPassword("");
        return user;
    }
}
