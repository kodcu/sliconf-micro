package javaday.istanbul.sliconf.micro.util;

/**
 * Created by ttayfur on 7/31/17.
 */
public class Constants {

    public static final int MIN_PASS_LENGTH = 8;
    public static final int MAX_PASS_LENGTH = 20;

    public static final int PASS_RESET_TIME_TO_LIVE_MINUTE = 5;

    public static final int MAX_UPLOADED_FILE_SIZE = 3145728; // 1024 * 1204 = 3MB

    public static final int EVENT_KEY_LENGTH = 4;

    public static final String DEFAULT_USER_FULLNAME = "Guest";

    private Constants() {
    }
}
