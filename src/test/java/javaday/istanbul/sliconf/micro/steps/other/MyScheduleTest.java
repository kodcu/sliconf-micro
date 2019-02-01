package javaday.istanbul.sliconf.micro.steps.other;

import cucumber.api.java.Before;
import cucumber.api.java.tr.Diyelimki;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.agenda.model.AgendaElement;
import javaday.istanbul.sliconf.micro.event.EventBuilder;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.event.model.LifeCycleState;
import javaday.istanbul.sliconf.micro.event.service.EventRepositoryService;
import javaday.istanbul.sliconf.micro.floor.Floor;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.room.Room;
import javaday.istanbul.sliconf.micro.speaker.Speaker;
import javaday.istanbul.sliconf.micro.user.util.UserBuilder;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.user.model.UserScheduleElement;
import javaday.istanbul.sliconf.micro.user.schedule.UserScheduleRepositoryService;
import javaday.istanbul.sliconf.micro.user.schedule.controller.AddToScheduleRoute;
import javaday.istanbul.sliconf.micro.user.schedule.controller.ListScheduleRoute;
import javaday.istanbul.sliconf.micro.user.schedule.controller.RemoveFromScheduleRoute;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import javaday.istanbul.sliconf.micro.util.TestUtil;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

@Ignore
public class MyScheduleTest extends SpringBootTestConfig { // NOSONAR

    @Autowired
    UserRepositoryService userRepositoryService;

    @Autowired
    EventRepositoryService eventRepositoryService;

    @Autowired
    UserScheduleRepositoryService userScheduleRepositoryService;

    @Autowired
    AddToScheduleRoute addToScheduleRoute;

    @Autowired
    RemoveFromScheduleRoute removeFromScheduleRoute;


    @Autowired
    ListScheduleRoute listScheduleRoute;


    private List<Speaker> speakers = new ArrayList<>();
    private List<AgendaElement> agendaElements = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private List<Floor> floors = new ArrayList<>();

    private Event event1 = new Event();
    private User user1 = new User();




    @Diyelimki("^Kullanici gitmek istedigi konusmaya abone oluyor$")
    public void kullaniciGitmekIstedigiKonusmayaAboneOluyor() throws Throwable {
        // Given

        User user = new UserBuilder()
                .setName("userSchedule")
                .setEmail("userSchedule@sliconf.com")
                .setPassword("123123123")
                .build();

        ResponseMessage userSaveMessage = userRepositoryService.saveUser(user);

        assertTrue(userSaveMessage.isStatus());

        this.user1 = (User) userSaveMessage.getReturnObject();


        Event event = new EventBuilder()
                .setName("User Schedule Event Test")
                .setKey("USET1")
                .setExecutiveUser(this.user1.getId())
                .setDate(LocalDateTime.now().plusMonths(1))
                .build();

        TestUtil.generateFields(floors, rooms, speakers, agendaElements);

        event.setFloorPlan(floors);
        event.setRooms(rooms);
        event.setSpeakers(speakers);
        event.setAgenda(agendaElements);

        LifeCycleState lifeCycleState = new LifeCycleState();
        lifeCycleState.setEventStatuses(new HashSet<>());
        event.setLifeCycleState(lifeCycleState);

        ResponseMessage eventSaveMessage = eventRepositoryService.save(event);

        assertTrue(eventSaveMessage.isStatus());

        this.event1 = (Event) eventSaveMessage.getReturnObject();

        // True
        UserScheduleElement userScheduleElement1 = new UserScheduleElement();
        userScheduleElement1.setEventId(event1.getId());
        userScheduleElement1.setSessionId("agenda-element-1");
        userScheduleElement1.setUserId(user1.getId());

        UserScheduleElement userScheduleElement3 = new UserScheduleElement();
        userScheduleElement3.setEventId(event1.getId());
        userScheduleElement3.setSessionId("agenda-element-2");
        userScheduleElement3.setUserId(user1.getId());

        // False
        UserScheduleElement userScheduleElement2 = new UserScheduleElement();
        userScheduleElement2.setEventId(event1.getId());
        userScheduleElement2.setSessionId("agenda-element-1");
        userScheduleElement2.setUserId(user1.getId());

        UserScheduleElement userScheduleElement4 = new UserScheduleElement();
        userScheduleElement4.setEventId("");
        userScheduleElement4.setSessionId("agenda-element-1");
        userScheduleElement4.setUserId(user1.getId());

        UserScheduleElement userScheduleElement5 = new UserScheduleElement();
        userScheduleElement5.setEventId(null);
        userScheduleElement5.setSessionId("agenda-element-1");
        userScheduleElement5.setUserId(user1.getId());

        UserScheduleElement userScheduleElement6 = new UserScheduleElement();
        userScheduleElement6.setEventId("");
        userScheduleElement6.setSessionId("");
        userScheduleElement6.setUserId(user1.getId());

        UserScheduleElement userScheduleElement7 = new UserScheduleElement();
        userScheduleElement7.setEventId("");
        userScheduleElement7.setSessionId(null);
        userScheduleElement7.setUserId(user1.getId());

        UserScheduleElement userScheduleElement11 = new UserScheduleElement();
        userScheduleElement11.setEventId("");
        userScheduleElement11.setSessionId(null);
        userScheduleElement11.setUserId("");

        UserScheduleElement userScheduleElement8 = new UserScheduleElement();
        userScheduleElement8.setEventId("");
        userScheduleElement8.setSessionId(null);
        userScheduleElement8.setUserId(null);

        UserScheduleElement userScheduleElement9 = new UserScheduleElement();
        userScheduleElement9.setEventId("");
        userScheduleElement9.setSessionId("");
        userScheduleElement9.setUserId("");

        UserScheduleElement userScheduleElement10 = new UserScheduleElement();
        userScheduleElement10.setEventId("");
        userScheduleElement10.setSessionId("");
        userScheduleElement10.setUserId(null);

        UserScheduleElement userScheduleElement12 = new UserScheduleElement();
        userScheduleElement12.setEventId(event1.getId());
        userScheduleElement12.setSessionId("");
        userScheduleElement12.setUserId(null);

        UserScheduleElement userScheduleElement13 = new UserScheduleElement();
        userScheduleElement13.setEventId(event1.getId());
        userScheduleElement13.setSessionId(null);
        userScheduleElement13.setUserId(null);

        UserScheduleElement userScheduleElement14 = new UserScheduleElement();
        userScheduleElement14.setEventId(event1.getId());
        userScheduleElement14.setSessionId("agenda-element-1");
        userScheduleElement14.setUserId("");

        UserScheduleElement userScheduleElement15 = new UserScheduleElement();
        userScheduleElement15.setEventId(event1.getId());
        userScheduleElement15.setSessionId("agenda-element-1");
        userScheduleElement15.setUserId(null);

        UserScheduleElement userScheduleElement16 = new UserScheduleElement();
        userScheduleElement16.setEventId("eventid123-event12");
        userScheduleElement16.setSessionId("agenda-element-1");
        userScheduleElement16.setUserId(this.user1.getId());

        UserScheduleElement userScheduleElement17 = new UserScheduleElement();
        userScheduleElement17.setEventId(this.event1.getId());
        userScheduleElement17.setSessionId("agenda-element-1444");
        userScheduleElement17.setUserId(this.user1.getId());

        UserScheduleElement userScheduleElement18 = new UserScheduleElement();
        userScheduleElement18.setEventId(this.event1.getId());
        userScheduleElement18.setSessionId("agenda-element-1");
        userScheduleElement18.setUserId("useridnotfound123124");


        // When
        // True
        ResponseMessage addToScheduleMessage1 = addToScheduleRoute.addToSchedule(userScheduleElement1);
        ResponseMessage addToScheduleMessage3 = addToScheduleRoute.addToSchedule(userScheduleElement3);

        // False
        ResponseMessage addToScheduleMessage2 = addToScheduleRoute.addToSchedule(userScheduleElement2);
        ResponseMessage addToScheduleMessage4 = addToScheduleRoute.addToSchedule(userScheduleElement4);
        ResponseMessage addToScheduleMessage5 = addToScheduleRoute.addToSchedule(userScheduleElement5);
        ResponseMessage addToScheduleMessage6 = addToScheduleRoute.addToSchedule(userScheduleElement6);
        ResponseMessage addToScheduleMessage7 = addToScheduleRoute.addToSchedule(userScheduleElement7);
        ResponseMessage addToScheduleMessage8 = addToScheduleRoute.addToSchedule(userScheduleElement8);
        ResponseMessage addToScheduleMessage9 = addToScheduleRoute.addToSchedule(userScheduleElement9);
        ResponseMessage addToScheduleMessage10 = addToScheduleRoute.addToSchedule(userScheduleElement10);
        ResponseMessage addToScheduleMessage11 = addToScheduleRoute.addToSchedule(userScheduleElement11);
        ResponseMessage addToScheduleMessage12 = addToScheduleRoute.addToSchedule(userScheduleElement12);
        ResponseMessage addToScheduleMessage13 = addToScheduleRoute.addToSchedule(userScheduleElement13);
        ResponseMessage addToScheduleMessage14 = addToScheduleRoute.addToSchedule(userScheduleElement14);
        ResponseMessage addToScheduleMessage15 = addToScheduleRoute.addToSchedule(userScheduleElement15);
        ResponseMessage addToScheduleMessage16 = addToScheduleRoute.addToSchedule(userScheduleElement16);
        ResponseMessage addToScheduleMessage17 = addToScheduleRoute.addToSchedule(userScheduleElement17);
        ResponseMessage addToScheduleMessage18 = addToScheduleRoute.addToSchedule(userScheduleElement18);


        // Then
        assertTrue(addToScheduleMessage1.isStatus());
        assertTrue(addToScheduleMessage3.isStatus());

        assertFalse(addToScheduleMessage2.isStatus());
        assertFalse(addToScheduleMessage4.isStatus());
        assertFalse(addToScheduleMessage5.isStatus());
        assertFalse(addToScheduleMessage6.isStatus());
        assertFalse(addToScheduleMessage7.isStatus());
        assertFalse(addToScheduleMessage8.isStatus());
        assertFalse(addToScheduleMessage9.isStatus());
        assertFalse(addToScheduleMessage10.isStatus());
        assertFalse(addToScheduleMessage11.isStatus());
        assertFalse(addToScheduleMessage12.isStatus());
        assertFalse(addToScheduleMessage13.isStatus());
        assertFalse(addToScheduleMessage14.isStatus());
        assertFalse(addToScheduleMessage15.isStatus());
        assertFalse(addToScheduleMessage16.isStatus());
        assertFalse(addToScheduleMessage17.isStatus());
        assertFalse(addToScheduleMessage18.isStatus());

    }

    @Diyelimki("^Kullanici abone oldugu konusmayi kaldiriyor$")
    public void kullaniciAboneOlduguKonusmayiKaldiriyor() throws Throwable {
        // Given
        List<UserScheduleElement> userScheduleElements = userScheduleRepositoryService.findAll();

        assertNotNull(userScheduleElements);
        assertFalse(userScheduleElements.isEmpty());

        UserScheduleElement userScheduleElement1 = userScheduleElements.get(0);
        userScheduleElement1.setId(userScheduleElements.get(0).getId());
        userScheduleElement1.setSessionId(userScheduleElements.get(0).getSessionId());
        userScheduleElement1.setEventId(userScheduleElements.get(0).getEventId());
        userScheduleElement1.setUserId(userScheduleElements.get(0).getUserId());

        String id = userScheduleElements.get(1).getId();
        String eventId = userScheduleElements.get(1).getEventId();
        String sessionId = userScheduleElements.get(1).getSessionId();
        String userId = userScheduleElements.get(1).getUserId();

        UserScheduleElement userScheduleElement2 = new UserScheduleElement();
        userScheduleElement2.setId(id);
        userScheduleElement2.setSessionId("sdasd");
        userScheduleElement2.setEventId(eventId);
        userScheduleElement2.setUserId(userId);

        UserScheduleElement userScheduleElement6 = new UserScheduleElement();
        userScheduleElement6.setId(id);
        userScheduleElement6.setSessionId(null);
        userScheduleElement6.setEventId(eventId);
        userScheduleElement6.setUserId(userId);

        UserScheduleElement userScheduleElement3 = new UserScheduleElement();
        userScheduleElement3.setId(id);
        userScheduleElement3.setSessionId(sessionId);
        userScheduleElement3.setEventId("adsasd");
        userScheduleElement3.setUserId(userId);

        UserScheduleElement userScheduleElement7 = new UserScheduleElement();
        userScheduleElement7.setId(id);
        userScheduleElement7.setSessionId(sessionId);
        userScheduleElement7.setEventId(null);
        userScheduleElement7.setUserId(userId);

        UserScheduleElement userScheduleElement4 = new UserScheduleElement();
        userScheduleElement4.setId("adasd");
        userScheduleElement4.setSessionId(sessionId);
        userScheduleElement4.setEventId(eventId);
        userScheduleElement4.setUserId(userId);

        UserScheduleElement userScheduleElement8 = new UserScheduleElement();
        userScheduleElement8.setId(null);
        userScheduleElement8.setSessionId(sessionId);
        userScheduleElement8.setEventId(eventId);
        userScheduleElement8.setUserId(userId);

        UserScheduleElement userScheduleElement5 = new UserScheduleElement();
        userScheduleElement5.setId(id);
        userScheduleElement5.setSessionId(sessionId);
        userScheduleElement5.setEventId(eventId);
        userScheduleElement5.setUserId("sdasd");

        UserScheduleElement userScheduleElement9 = new UserScheduleElement();
        userScheduleElement9.setId(id);
        userScheduleElement9.setSessionId(sessionId);
        userScheduleElement9.setEventId(eventId);
        userScheduleElement9.setUserId(null);

        // When
        ResponseMessage removeFromScheduleMessage1 = removeFromScheduleRoute.removeFromSchedule(userScheduleElement1);

        ResponseMessage removeFromScheduleMessage2 = removeFromScheduleRoute.removeFromSchedule(userScheduleElement2);
        ResponseMessage removeFromScheduleMessage3 = removeFromScheduleRoute.removeFromSchedule(userScheduleElement3);
        ResponseMessage removeFromScheduleMessage4 = removeFromScheduleRoute.removeFromSchedule(userScheduleElement4);
        ResponseMessage removeFromScheduleMessage5 = removeFromScheduleRoute.removeFromSchedule(userScheduleElement5);
        ResponseMessage removeFromScheduleMessage6 = removeFromScheduleRoute.removeFromSchedule(userScheduleElement6);
        ResponseMessage removeFromScheduleMessage7 = removeFromScheduleRoute.removeFromSchedule(userScheduleElement7);
        ResponseMessage removeFromScheduleMessage8 = removeFromScheduleRoute.removeFromSchedule(userScheduleElement8);
        ResponseMessage removeFromScheduleMessage9 = removeFromScheduleRoute.removeFromSchedule(userScheduleElement9);


        // Then
        assertTrue(removeFromScheduleMessage1.isStatus());

        assertFalse(removeFromScheduleMessage2.isStatus());
        assertFalse(removeFromScheduleMessage3.isStatus());
        assertFalse(removeFromScheduleMessage4.isStatus());
        assertFalse(removeFromScheduleMessage5.isStatus());
        assertFalse(removeFromScheduleMessage6.isStatus());
        assertFalse(removeFromScheduleMessage7.isStatus());
        assertFalse(removeFromScheduleMessage8.isStatus());
        assertFalse(removeFromScheduleMessage9.isStatus());

    }

    @Diyelimki("^Kullanici gitmek istedigi konusmalari listeliyor$")
    public void kullaniciGitmekIstedigiKonusmalariListeliyor() throws Throwable {
        // Given

        // Given

        User user = new UserBuilder()
                .setName("userSchedule")
                .setEmail("userSchedule@sliconf.com")
                .setPassword("123123123")
                .build();

        ResponseMessage userSaveMessage = userRepositoryService.saveUser(user);

        assertTrue(userSaveMessage.isStatus());

        this.user1 = (User) userSaveMessage.getReturnObject();


        Event event = new EventBuilder()
                .setName("User Schedule Event Test")
                .setKey("USET1")
                .setExecutiveUser(this.user1.getId())
                .setDate(LocalDateTime.now().plusMonths(1))
                .build();

        TestUtil.generateFields(floors, rooms, speakers, agendaElements);

        event.setFloorPlan(floors);
        event.setRooms(rooms);
        event.setSpeakers(speakers);
        event.setAgenda(agendaElements);

        LifeCycleState lifeCycleState = new LifeCycleState();
        lifeCycleState.setEventStatuses(new HashSet<>());
        event.setLifeCycleState(lifeCycleState);

        ResponseMessage eventSaveMessage = eventRepositoryService.save(event);

        assertTrue(eventSaveMessage.isStatus());

        this.event1 = (Event) eventSaveMessage.getReturnObject();

        // When
        ResponseMessage listResponseMessage1 = listScheduleRoute.listSchedule(this.user1.getId(), this.event1.getId());
        ResponseMessage listResponseMessage2 = listScheduleRoute.listSchedule(this.user1.getId(), null);
        ResponseMessage listResponseMessage3 = listScheduleRoute.listSchedule(this.user1.getId(), "");
        ResponseMessage listResponseMessage4 = listScheduleRoute.listSchedule(null, this.event1.getId());
        ResponseMessage listResponseMessage5 = listScheduleRoute.listSchedule("", this.event1.getId());
        ResponseMessage listResponseMessage6 = listScheduleRoute.listSchedule(null, null);
        ResponseMessage listResponseMessage7 = listScheduleRoute.listSchedule(null, "");
        ResponseMessage listResponseMessage8 = listScheduleRoute.listSchedule("", "");
        ResponseMessage listResponseMessage9 = listScheduleRoute.listSchedule("", null);
        ResponseMessage listResponseMessage10 = listScheduleRoute.listSchedule("asdasd", this.event1.getId());
        ResponseMessage listResponseMessage11 = listScheduleRoute.listSchedule("asdasd", "");
        ResponseMessage listResponseMessage12 = listScheduleRoute.listSchedule(this.user1.getId(), "asdasfas");
        ResponseMessage listResponseMessage13 = listScheduleRoute.listSchedule("", "asdasfas");

        // Then
        assertTrue(listResponseMessage1.isStatus());

        assertFalse(listResponseMessage2.isStatus());
        assertFalse(listResponseMessage3.isStatus());
        assertFalse(listResponseMessage4.isStatus());
        assertFalse(listResponseMessage5.isStatus());
        assertFalse(listResponseMessage6.isStatus());
        assertFalse(listResponseMessage7.isStatus());
        assertFalse(listResponseMessage8.isStatus());
        assertFalse(listResponseMessage9.isStatus());
        assertFalse(listResponseMessage10.isStatus());
        assertFalse(listResponseMessage11.isStatus());
        assertFalse(listResponseMessage12.isStatus());
        assertFalse(listResponseMessage13.isStatus());
    }
}
