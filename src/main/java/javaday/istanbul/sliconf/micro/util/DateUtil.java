package javaday.istanbul.sliconf.micro.util;

import java.time.LocalDateTime;
import java.util.Objects;

public class DateUtil {

    private DateUtil() {
        // private constructor for static
    }

    /**
     * Eger firstDateTime ve ya SecondDateTime null ise false doner
     *
     * Eger firstDateTime ve SecondDateTime null degil ise
     * firstDateTime, secondDateTime a esit ve ya once ise true doner
     *
     * Eger firstDateTime ve SecondDateTime null degil ise
     * firstDateTime, secondDateTime a esit ve ya once degil ise false doner
     *
     * @param firstDateTime
     * @param secondDateTime
     * @return
     */
    public static boolean isFirstDateTimeBeforeOrEqualSecondDateTime(LocalDateTime firstDateTime, LocalDateTime secondDateTime) {
        if (Objects.nonNull(firstDateTime) && Objects.nonNull(secondDateTime)) {
            return firstDateTime.isBefore(secondDateTime) || firstDateTime.isEqual(secondDateTime);
        }
        return false;
    }
}
