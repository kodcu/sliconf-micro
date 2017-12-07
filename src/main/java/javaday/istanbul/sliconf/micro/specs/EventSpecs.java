package javaday.istanbul.sliconf.micro.specs;

import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.util.Constants;
import javaday.istanbul.sliconf.micro.util.RandomGenerator;

import java.time.LocalDateTime;
import java.util.Objects;


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
     *
     * @param event
     * @return
     */
    public static boolean checkIfEventDateAfterOrInNow(Event event) {
        LocalDateTime now = LocalDateTime.now();

        if (Objects.nonNull(event) && Objects.nonNull(event.getStartDate())) {
            return now.isBefore(event.getStartDate());
        }

        return false;
    }

    /**
     * Event'in tarihi verilen tarihten sonra mi kontrolu
     *
     * @param event
     * @return
     */
    public static boolean checkIfEventDateAfterFromGivenDate(Event event, LocalDateTime dateTime) {
        return dateTime.isBefore(event.getStartDate());
    }

    public static String generateKanbanNumber(Event event, EventRepositoryService eventRepositoryService) {
        boolean isKeyUnique = false;
        String key;

        while (!isKeyUnique) {
            key = RandomGenerator.generateRandom(Constants.EVENT_KEY_LENGTH);

            if (!eventRepositoryService.isKeyExists(key)) {
                event.setKey(key);
                isKeyUnique = true;
            }
        }

        return event.getKey();
    }
}
