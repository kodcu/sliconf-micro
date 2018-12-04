package javaday.istanbul.sliconf.micro.user.util;

import javaday.istanbul.sliconf.micro.util.Constants;

import java.util.Objects;

public class UserSpecs {


    private UserSpecs() {
        // For static class added private constructor
    }

    public static boolean checkUserParams(String param, int nameLength) {
        return param.length() >= nameLength;
    }

    /**
     * Gelen 2 password ilk once null mu degil mi diye kontrol sonra ise
     * Buyuk/Kucuk harf duyarli olarak karsilastirir
     *
     * @param password
     * @param passwordAgain
     * @return
     */
    public static boolean isPasswordsEquals(String password, String passwordAgain) {
        return Objects.nonNull(password) && Objects.nonNull(passwordAgain) && password.equals(passwordAgain);
    }

    /**
     * Eger pass Constants.MIN_PASS_LENGTH ten buyuk ve ya esitse ve
     * Constants.MAX_PASS_LENGTH ten kucuk ve ya esitse true doner
     *
     * @param pass
     * @return
     */
    public static boolean isPassMeetRequiredLengths(String pass) {
        return Objects.nonNull(pass) &&
                pass.length() >= Constants.MIN_PASS_LENGTH &&
                pass.length() <= Constants.MAX_PASS_LENGTH;
    }


}
