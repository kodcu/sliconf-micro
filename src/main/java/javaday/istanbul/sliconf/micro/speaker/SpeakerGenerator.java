package javaday.istanbul.sliconf.micro.speaker;

import com.devskiller.jfairy.Fairy;
import javaday.istanbul.sliconf.micro.event.model.Event;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class SpeakerGenerator {

    private SpeakerGenerator() {}

    public static void generateRandomSpeakers(int count, Event event) {

        Fairy fairy = Fairy.create();

        List<Speaker> speakers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Speaker speaker = Speaker.builder()
                    .about(fairy.textProducer().latinSentence(fairy.baseProducer().randomBetween(30, 150)))
                    .id(new ObjectId().toString())
                    .linkedin(fairy.textProducer().latinWord(fairy.baseProducer().randomBetween(4, 10)))
                    .name(fairy.person().getFullName())
                    .twitter(fairy.textProducer().latinWord(fairy.baseProducer().randomBetween(4, 10)))
                    .workingAt(fairy.company().getName())
                    .topics(new ArrayList<>())
                    .build();
            speakers.add(speaker);
        }
        event.setSpeakers((speakers));
    }

}
