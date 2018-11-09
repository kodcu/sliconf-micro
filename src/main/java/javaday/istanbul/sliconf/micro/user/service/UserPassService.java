package javaday.istanbul.sliconf.micro.user.service;

import javaday.istanbul.sliconf.micro.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;


/**
 * Created by ttayfur on 7/6/17.
 */
public class UserPassService {
    private Logger logger = LoggerFactory.getLogger(UserPassService.class);

    private PasswordEncryptionService encryptionService = new PasswordEncryptionService();

    /**
     * Password islemlerinde kullanilmak uzere yeni bir Salt olusturur
     *
     * @return
     */
    private byte[] getSalt() {
        byte[] salt = null;

        try {
            salt = encryptionService.generateSalt();
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
        }

        return salt;
    }

    /**
     * Salt ve verilen password ile hashedPassword olusturur
     *
     * @param password
     * @param salt
     * @return
     */
    private byte[] getHashedPassword(String password, byte[] salt) {
        byte[] ePass = null;

        try {
            ePass = encryptionService.getEncryptedPassword(password, salt);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error(e.getMessage(), e);
        }
        return ePass;
    }

    /**
     * Girilen password un hashedPassword ve salt ile uyusup uyusmasdigi kontrolu yapar
     *
     * @param password
     * @param hashedPassword
     * @param salt
     * @return
     */
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
     * @param sourceUser db den cekilmis salt bulunduran user
     * @param targetUser sifresi bulunan ve db user ile karsilastirilacak user
     * @return
     */
    public boolean checkIfUserAuthenticated(User sourceUser, User targetUser) {
        byte[] salt = Objects.nonNull(sourceUser) ? sourceUser.getSalt() : null;
        byte[] hashedPassword = Objects.nonNull(sourceUser) ? sourceUser.getHashedPassword() : null;

        if (Objects.isNull(salt) || Objects.isNull(hashedPassword) || Objects.isNull(targetUser) || Objects.isNull(targetUser.getPassword())) {
            return false;
        }

        return checkPassword(targetUser.getPassword(), hashedPassword, salt);
    }

    /**
     * Verilen User in passwordu ile hashed password olusturur ve user string passwordunu siler
     * Daha sonraki auth islemlerinde hashed password ve salt kullanilacaktir
     *
     * @param user
     * @return
     */
    public User createNewUserWithHashedPassword(User user) {
        byte[] salt = getSalt();
        byte[] hashedPassword = getHashedPassword(user.getPassword(), salt);

        user.setSalt(salt);
        user.setHashedPassword(hashedPassword);

        user.setPassword("");
        return user;
    }
}
