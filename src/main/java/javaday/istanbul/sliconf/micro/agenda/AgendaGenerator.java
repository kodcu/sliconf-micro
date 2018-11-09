package javaday.istanbul.sliconf.micro.agenda;

import com.devskiller.jfairy.Fairy;
import com.google.api.client.util.Lists;
import com.google.common.base.Splitter;
import javaday.istanbul.sliconf.micro.agenda.model.AgendaElement;
import javaday.istanbul.sliconf.micro.event.model.Event;
import org.bson.types.ObjectId;

import java.util.*;

public class AgendaGenerator {

    public static void generateRandomAgendaElements(int count, Event event) {

        Fairy fairy = Fairy.create();
        List<AgendaElement> agendaElements = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            List<String> tags = Lists.newArrayList(Splitter.on(" , ").split((fairy.textProducer().latinSentence(100))));

            AgendaElement agendaElement = AgendaElement.builder()
                    .date(fairy.dateProducer().randomDateBetweenTwoDates(event.getStartDate(), event.getEndDate()))
                    .detail(fairy.textProducer().latinSentence(fairy.baseProducer().randomBetween(50,300)))
                    .duration(fairy.baseProducer().randomElement(Lists.newArrayList(Arrays.asList(30, 45, 60))))
                    .id(new ObjectId().toString())
                    .level(fairy.baseProducer().randomElement(Arrays.asList(0, 1, 2)))
                    .room(fairy.baseProducer().randomElement(event.getRooms()).getId())
                    .speaker(fairy.baseProducer().randomElement(event.getSpeakers()).getId())
                    .topic(fairy.textProducer().latinSentence(fairy.baseProducer().randomBetween(8,25)))
                    .star(0)
                    .tags(Lists.newArrayList(fairy.baseProducer().randomElements(tags, fairy.baseProducer().randomBetween(1,8))))
                    .voteCount(0)
                    .build();

            agendaElements.add(agendaElement);
        }
        event.setAgenda(agendaElements);
    }
}
