package javaday.istanbul.sliconf.micro.event;

import com.devskiller.jfairy.Fairy;
import javaday.istanbul.sliconf.micro.event.model.About;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.event.model.LifeCycleState;
import javaday.istanbul.sliconf.micro.event.model.Location;

import java.time.LocalDateTime;
import java.util.*;

public class EventGenerator {

    public static Set<Event> generateRandomEvents(int count, String userId) {

        Fairy fairy = Fairy.create();
        Set<Event> events = new HashSet<>();

        for (int i = 0; i < count; i++) {
            LocalDateTime startDate = fairy.dateProducer().randomDateInTheFuture(1);
            Event event = new EventBuilder()
                    .setDate(startDate)
                    .setEndDate(fairy.dateProducer().randomDateBetweenTwoDates(startDate, startDate.plusWeeks(1)))
                    .setExecutiveUser(userId)
                    .setName(fairy.textProducer().latinSentence(fairy.baseProducer().randomBetween(1,5)))
                    .build();
            event.setDescription(fairy.textProducer().latinSentence(fairy.baseProducer().randomBetween(45,200)));

            Map<String, String> social = new HashMap<>();
            social.put("youtube", fairy.textProducer().latinWord(fairy.baseProducer().randomBetween(4,10)));
            social.put("twitter", fairy.textProducer().latinWord(fairy.baseProducer().randomBetween(4,10)));
            social.put("facebook", fairy.textProducer().latinWord(fairy.baseProducer().randomBetween(4,10)));
            social.put("instagram", fairy.textProducer().latinWord(fairy.baseProducer().randomBetween(4,10)));

            Location location = Location.builder()
                    .description(fairy.textProducer().latinSentence(fairy.baseProducer().randomBetween(15,300)))
                    .venue(fairy.textProducer().latinSentence(fairy.baseProducer().randomBetween(1,5)))
                    .lat(String.valueOf(fairy.baseProducer().randomBetween(-90.0, 90.0)))
                    .lng(String.valueOf(fairy.baseProducer().randomBetween(-180.0, 180.0)))
                    .build();

            About about = About.builder()
                    .email(fairy.company().getEmail())
                    .web(fairy.company().getUrl())
                    .phone(Collections.singletonList(fairy.baseProducer().numerify("0123456789")))
                    .social(social)
                    .location(location)
                    .build();

            LifeCycleState lifeCycleState = new LifeCycleState();
            List<LifeCycleState.EventStatus> eventStatuses = new ArrayList<>();
            lifeCycleState.setEventStatuses(eventStatuses);

            event.setLifeCycleState(lifeCycleState);
            event.setAbout(about);
            events.add(event);
        }
        return events;
    }
}
