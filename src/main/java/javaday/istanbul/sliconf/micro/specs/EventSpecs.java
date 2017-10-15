package javaday.istanbul.sliconf.micro.specs;

import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.util.RandomGenerator;

import java.time.LocalDateTime;


/**
 * Event ile ilgili cok kullanilan isleri barindiran sinif
 */
public class EventSpecs {

    private EventSpecs() {
        // private constructor for static
    }

    /**
     * Event name'in minimum uzunlugunu gelen parametreye gore kontrol eder
     *
     * @param event
     * @param nameLength
     * @return
     */
    public static boolean checkEventName(Event event, int nameLength) {
        return event.getName().length() >= nameLength;
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

    /**
     * Event'in tarihi verilen tarihten sonra mi kontrolu
     * @param event
     * @return
     */
    public static boolean checkIfEventDateAfterFromGivenDate(Event event, LocalDateTime dateTime) {
        return dateTime.isBefore(event.getDate());
    }

    public static String generateKanbanNumber(Event event) {
        String key = RandomGenerator.generateRandom(4);
        event.setKey(key);
        return event.getKey();
    }
}
