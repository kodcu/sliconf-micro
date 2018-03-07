package javaday.istanbul.sliconf.micro.steps;


import cucumber.api.java.tr.Diyelimki;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.builder.EventBuilder;
import javaday.istanbul.sliconf.micro.controller.event.agenda.CreateAgendaRoute;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.event.Floor;
import javaday.istanbul.sliconf.micro.model.event.Room;
import javaday.istanbul.sliconf.micro.model.event.Speaker;
import javaday.istanbul.sliconf.micro.model.event.agenda.AgendaElement;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.specs.EventSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@ContextConfiguration(classes = {CucumberConfiguration.class})
@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class CreateAgendaTest {// NOSONAR

    @Autowired
    UserRepositoryService userRepositoryService;

    @Autowired
    EventRepositoryService eventRepositoryService;

    @Autowired
    CreateAgendaRoute createAgendaRoute;


    @Diyelimki("^Agenda kaydediliyor$")
    public void agendakaydediliyor() throws Throwable {
        // Given
        User user = new User();
        user.setUsername("createAgendaUser1");
        user.setEmail("createAgendaUser1@sliconf.com");
        user.setPassword("123123123");

        ResponseMessage savedUserMessage = userRepositoryService.saveUser(user);

        assertTrue(savedUserMessage.isStatus());

        String userId = ((User) savedUserMessage.getReturnObject()).getId();

        Event event = new EventBuilder().setName("Create Agenda Test")
                .setExecutiveUser(userId)
                .setDate(LocalDateTime.now().plusMonths(2)).build();

        EventSpecs.generateKanbanNumber(event, eventRepositoryService);
        event.setStatus(true);

        event.setSpeakers(createSpeakers1());
        event.setRooms(createRooms1());
        event.setFloorPlan(createFLoors1());

        ResponseMessage eventSaveMessage = eventRepositoryService.save(event);
        String eventKey = ((Event) eventSaveMessage.getReturnObject()).getKey();


        List<AgendaElement> agendaElements1 = createAgendaElements1();

        // When

        // True
        ResponseMessage saveAgendaResponseMessage1 = createAgendaRoute.saveAgenda(agendaElements1, eventKey);

        // False
        ResponseMessage saveAgendaResponseMessage2 = createAgendaRoute.saveAgenda(agendaElements1, null);
        ResponseMessage saveAgendaResponseMessage3 = createAgendaRoute.saveAgenda(agendaElements1, "9999");
        ResponseMessage saveAgendaResponseMessage4 = createAgendaRoute.saveAgenda(agendaElements1, "");
        ResponseMessage saveAgendaResponseMessage5 = createAgendaRoute.saveAgenda(null, "");

        agendaElements1.add(null);
        ResponseMessage saveAgendaResponseMessage6 = createAgendaRoute.saveAgenda(agendaElements1, "");


        // Then
        assertTrue(saveAgendaResponseMessage1.isStatus());

        assertFalse(saveAgendaResponseMessage2.isStatus());
        assertFalse(saveAgendaResponseMessage3.isStatus());
        assertFalse(saveAgendaResponseMessage4.isStatus());
        assertFalse(saveAgendaResponseMessage5.isStatus());
        assertFalse(saveAgendaResponseMessage6.isStatus());
    }

    private List<AgendaElement> createAgendaElements1() {
        List<AgendaElement> agendaElements = new ArrayList<>();

        AgendaElement agendaElement1 = new AgendaElement();
        agendaElement1.setRoom("room1");
        agendaElement1.setSpeaker("speaker1");
        agendaElement1.setTopic("topic1");
        agendaElement1.setLevel(0);
        agendaElement1.setDetail("detail1");
        agendaElement1.setDuration(30);
        agendaElement1.setDate(LocalDateTime.now().plusMonths(2).plusHours(1));
        agendaElement1.setTags(Arrays.asList("tag1", "tag2", "tag3"));

        AgendaElement agendaElement2 = new AgendaElement();
        agendaElement2.setRoom("room2");
        agendaElement2.setSpeaker("speaker2");
        agendaElement2.setTopic("topic2");
        agendaElement2.setLevel(1);
        agendaElement2.setDetail("detail2");
        agendaElement2.setDuration(30);
        agendaElement2.setDate(LocalDateTime.now().plusMonths(2).plusHours(2));
        agendaElement2.setTags(Arrays.asList("tag1", "tag2", "tag3"));

        AgendaElement agendaElement3 = new AgendaElement();
        agendaElement3.setRoom("room3");
        agendaElement3.setSpeaker("speaker3");
        agendaElement3.setTopic("topic3");
        agendaElement3.setLevel(2);
        agendaElement3.setDetail("detail3");
        agendaElement3.setDuration(30);
        agendaElement3.setDate(LocalDateTime.now().plusMonths(2).plusHours(3));
        agendaElement3.setTags(Arrays.asList("tag1", "tag2", "tag3"));


        agendaElements.add(agendaElement1);
        agendaElements.add(agendaElement2);
        agendaElements.add(agendaElement3);

        return agendaElements;
    }

    private List<Speaker> createSpeakers1() {
        List<Speaker> speakers = new ArrayList<>();

        Speaker speaker1 = new Speaker();
        speaker1.setId("speaker1");
        speaker1.setName("speaker1");
        speaker1.setWorkingAt("kodcu1");
        speaker1.setProfilePicture("profilePic1");
        speaker1.setAbout("about1");

        Speaker speaker2 = new Speaker();
        speaker2.setId("speaker2");
        speaker2.setName("speaker2");
        speaker2.setWorkingAt("kodcu2");
        speaker2.setProfilePicture("profilePic2");
        speaker2.setAbout("about2");

        Speaker speaker3 = new Speaker();
        speaker3.setId("speaker3");
        speaker3.setName("speaker3");
        speaker3.setWorkingAt("kodcu3");
        speaker3.setProfilePicture("profilePic3");
        speaker3.setAbout("about3");

        speakers.add(speaker1);
        speakers.add(speaker2);
        speakers.add(speaker3);

        return speakers;
    }

    private List<Room> createRooms1() {
        List<Room> roomList = new ArrayList<>();

        Room room1 = new Room();
        room1.setFloor("floor1");
        room1.setId("room1");
        room1.setLabel("Room 1");

        Room room2 = new Room();
        room2.setFloor("floor2");
        room2.setId("room2");
        room2.setLabel("Room 2");

        Room room3 = new Room();
        room3.setFloor("floor3");
        room3.setId("room3");
        room3.setLabel("Room 3");

        roomList.add(room1);
        roomList.add(room2);
        roomList.add(room2);

        return roomList;
    }

    private List<Floor> createFLoors1() {
        List<Floor> floors = new ArrayList<>();

        Floor floor1 = new Floor();
        floor1.setId("floor1");
        floor1.setName("Floor 1");
        floor1.setImage("floorImage1");

        Floor floor2 = new Floor();
        floor2.setId("floor2");
        floor2.setName("Floor 2");
        floor2.setImage("floorImage2");

        Floor floor3 = new Floor();
        floor3.setId("floor3");
        floor3.setName("Floor 3");
        floor3.setImage("floorImage3");

        floors.add(floor1);
        floors.add(floor2);
        floors.add(floor3);

        return floors;
    }

}
