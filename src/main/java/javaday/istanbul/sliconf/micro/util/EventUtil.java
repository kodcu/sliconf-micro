package javaday.istanbul.sliconf.micro.util;

import javaday.istanbul.sliconf.micro.model.event.Event;

import java.time.LocalDateTime;


/**
 * Event ile ilgili cok kullanilan isleri barindiran sinif
 */
public class EventUtil {

    /**
     * Event name'in minimum uzunlugunu gelen parametreye gore kontrol eder
     *
     * @param event
     * @param nameLength
     * @return
     */
    public static boolean checkEventName(Event event, int nameLength) {

        return event.getName().length() <= nameLength;
    }

    /**
     * Olusturulan eventin tarihi geri donuk olmamali
     * @param event
     * @return
     */
    public static boolean checkIfEventDateAfterOrInNow(Event event) {
        LocalDateTime now = LocalDateTime.now();

        return now.isBefore(event.getDate());
    }

    public static String generateKanbanNumber(Event event) {
        // Todo burayi duzenle daha iyi bir algoritma yaz
        event.setKey("K123");
        return event.getKey();
    }
}
