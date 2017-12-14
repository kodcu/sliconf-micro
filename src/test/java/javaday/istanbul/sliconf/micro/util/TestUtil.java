package javaday.istanbul.sliconf.micro.util;

import javaday.istanbul.sliconf.micro.model.event.Floor;
import javaday.istanbul.sliconf.micro.model.event.Room;
import javaday.istanbul.sliconf.micro.model.event.Speaker;
import javaday.istanbul.sliconf.micro.model.event.agenda.AgendaElement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestUtil {

    public static void generateFields(List<Floor> floors, List<Room> rooms, List<Speaker> speakers, List<AgendaElement> agendaElements) {
        generateFloors(floors);
        generateRooms(rooms);
        generateSpeakers(speakers);
        generateAgenda(agendaElements);
    }

    private static void generateFloors(List<Floor> floors) {
        Floor floor = new Floor();
        floor.setId("floor123");
        floor.setName("Floor 1");

        floors.add(floor);
    }

    private static void generateRooms(List<Room> rooms) {
        Room room = new Room();
        room.setId("room123");
        room.setFloor("floor123");
        room.setLabel("Room 1");

        rooms.add(room);
    }

    private static void generateSpeakers(List<Speaker> speakers) {
        Speaker speaker = new Speaker();
        speaker.setId("speaker123");
        speaker.setName("Test Speaker");
        speaker.setTopics(new ArrayList<>());

        speakers.add(speaker);
    }

    private static void generateAgenda(List<AgendaElement> agendaElements) {
        AgendaElement agendaElement = new AgendaElement();
        agendaElement.setId("agenda-element-1");
        agendaElement.setDate(LocalDateTime.now().minusSeconds(10));
        agendaElement.setDetail("Talk 123");
        agendaElement.setDuration(10);
        agendaElement.setLevel(0);
        agendaElement.setRoom("room123");
        agendaElement.setSpeaker("speaker123");
        agendaElement.setTopic("Topic 123");

        agendaElements.add(agendaElement);

        AgendaElement agendaElement1 = new AgendaElement();
        agendaElement1.setId("agenda-element-2");
        agendaElement1.setDate(LocalDateTime.now().minusMinutes(20));
        agendaElement1.setDetail("Talk 456");
        agendaElement1.setDuration(10);
        agendaElement1.setLevel(0);
        agendaElement1.setRoom("room123");
        agendaElement1.setSpeaker("speaker123");
        agendaElement1.setTopic("Topic 456");

        agendaElements.add(agendaElement1);
    }
}
