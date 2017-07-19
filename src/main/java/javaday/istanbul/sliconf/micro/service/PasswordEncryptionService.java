package javaday.istanbul.sliconf.micro.service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

/**
 * Created by ttayfur on 18.07.2017
 */
public class PasswordEncryptionService {

    private final String SECURE_RANDOM_INSTANCE = "SHA1PRNG";

    // PBKDF2 with SHA-1 as the hashing algorithm. Note that the NIST
    // specifically names SHA-1 as an acceptable hashing algorithm for PBKDF2
    private final String ALGORITHM = "PBKDF2WithHmacSHA1";

    // Pick an iteration count that works for you. The NIST recommends at
    // least 1,000 iterations:
    // http://csrc.nist.gov/publications/nistpubs/800-132/nist-sp800-132.pdf
    // iOS 4.x reportedly uses 10,000:
    // http://blog.crackpassword.com/2010/09/smartphone-forensics-cracking-blackberry-backup-passwords/
    private final short ITERATIONS = 20000;

    public boolean authenticate(String attemptedPassword, byte[] encryptedPassword, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        // Encrypt the clear-text password using the same salt that was used to
        // encrypt the original password
        byte[] encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt);

        // Authentication succeeds if encrypted password that the user entered
        // is equal to the stored hash
        return Arrays.equals(encryptedPassword, encryptedAttemptedPassword);
    }

    public byte[] getEncryptedPassword(String password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        // SHA-1 generates 160 bit hashes, so that's what makes sense here
        int derivedKeyLength = 160;

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, derivedKeyLength);
        SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITHM);

        return f.generateSecret(spec).getEncoded();
    }

    public byte[] generateSalt() throws NoSuchAlgorithmException {
        // VERY important to use SecureRandom instead of just Random
        SecureRandom random = SecureRandom.getInstance(SECURE_RANDOM_INSTANCE);

        // Generate a 8 byte (64 bit) salt as recommended by RSA PKCS5
        byte[] salt = new byte[8];
        random.nextBytes(salt);

        return salt;
    }
}
