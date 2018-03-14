package javaday.istanbul.sliconf.micro.util;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtil {

    // Email Regex java
    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

    // static Pattern object, since pattern is fixed
    private static Pattern pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    private EmailUtil() {
        // empty constructor for static
        throw new IllegalStateException("You can not create a new instance from this class!");
    }

    /**
     * This method validates the input email address with EMAIL_REGEX pattern
     *
     * @param email
     * @return boolean
     */
    public static boolean validateEmail(String email) {
        if (Objects.nonNull(email)) {
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }

        return false;
    }
}
