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

    public static final String DEFAULT_USER_ROLE = "ROLE_USER";


    public enum COMMENT_VOTES {
        DISLIKE(-1), NOTR(0), LIKE(1);

        private final int vote;

        COMMENT_VOTES(int vote) {
            this.vote = vote;
        }

        public int getValue() {
            return vote;
        }
    }


    private Constants() {
    }

    public static class Agenda {
        private Agenda() {
            // private constructor for static access
        }

        public static final int BREAK = -1;
        public static final int BEGINNER = 0;
        public static final int INTERMEDIATE = 1;
        public static final int ADVANCED = 2;
    }

}
