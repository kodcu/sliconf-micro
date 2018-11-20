package javaday.istanbul.sliconf.micro.steps.other;

import cucumber.api.java.tr.Diyelimki;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.agenda.controller.VoteAgendaElementRoute;
import javaday.istanbul.sliconf.micro.agenda.model.AgendaElement;
import javaday.istanbul.sliconf.micro.event.EventBuilder;
import javaday.istanbul.sliconf.micro.event.EventSpecs;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.event.model.LifeCycleState;
import javaday.istanbul.sliconf.micro.event.service.EventRepositoryService;
import javaday.istanbul.sliconf.micro.floor.Floor;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.room.Room;
import javaday.istanbul.sliconf.micro.speaker.Speaker;
import javaday.istanbul.sliconf.micro.user.model.User;
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
public class VoteAgendaElementTest extends SpringBootTestConfig { // NOSONAR

    @Autowired
    UserRepositoryService userRepositoryService;

    @Autowired
    EventRepositoryService eventRepositoryService;

    @Autowired
    VoteAgendaElementRoute voteAgendaElementRoute;

    private List<Speaker> speakers = new ArrayList<>();
    private List<AgendaElement> agendaElements = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private List<Floor> floors = new ArrayList<>();

    @Diyelimki("^Konusmaya Oy Veriliyor$")
    public void konusmayaOyVeriliyor() throws Throwable {

        User user = new User();
        user.setUsername("commentUserVoteAgenda1");
        user.setEmail("commentUserVoteAgenda1@sliconf.com");
        user.setPassword("123123123");
        ResponseMessage savedUserMessage = userRepositoryService.saveUser(user);

        assertTrue(savedUserMessage.isStatus());

        String userId = ((User) savedUserMessage.getReturnObject()).getId();

        User user2 = new User();
        user2.setUsername("commentUserVoteAgenda2");
        user2.setEmail("commentUserVoteAgenda2@sliconf.com");
        user2.setPassword("123123123");
        ResponseMessage savedUserMessage2 = userRepositoryService.saveUser(user2);

        assertTrue(savedUserMessage2.isStatus());

        String userId2 = ((User) savedUserMessage2.getReturnObject()).getId();

        User user3 = new User();
        user3.setUsername("commentUserVoteAgenda3");
        user3.setEmail("commentUserVoteAgenda3@sliconf.com");
        user3.setPassword("123123123");
        ResponseMessage savedUserMessage3 = userRepositoryService.saveUser(user3);

        assertTrue(savedUserMessage3.isStatus());

        String userId3 = ((User) savedUserMessage3.getReturnObject()).getId();

        Event event = new EventBuilder().setName("Vote Agenda Event")
                .setExecutiveUser(userId)
                .setDate(LocalDateTime.now().plusMonths(2)).build();

        EventSpecs.generateKanbanNumber(event, eventRepositoryService);

        TestUtil.generateFields(floors, rooms, speakers, agendaElements);

        event.setStatus(true);
        event.setFloorPlan(floors);
        event.setRooms(rooms);
        event.setSpeakers(speakers);
        event.setAgenda(agendaElements);
        LifeCycleState lifeCycleState = new LifeCycleState();
        lifeCycleState.setEventStatuses(new HashSet<>());
        event.setLifeCycleState(lifeCycleState);

        ResponseMessage eventSaveMessage = eventRepositoryService.save(event);
        String eventId = ((Event) eventSaveMessage.getReturnObject()).getId();
        String agendaElementId = "agenda-element-1";


        // When
        ResponseMessage responseMessage1 = voteAgendaElementRoute.voteAgenda("", agendaElementId, userId, "5");
        ResponseMessage responseMessage2 = voteAgendaElementRoute.voteAgenda(eventId, "", userId, "5");
        ResponseMessage responseMessage3 = voteAgendaElementRoute.voteAgenda(eventId, agendaElementId, "", "5");
        ResponseMessage responseMessage4 = voteAgendaElementRoute.voteAgenda(eventId, agendaElementId, userId, "0");
        ResponseMessage responseMessage5 = voteAgendaElementRoute.voteAgenda(eventId, agendaElementId, userId, "6");
        ResponseMessage responseMessage6 = voteAgendaElementRoute.voteAgenda(eventId, agendaElementId, userId, "asdas");
        ResponseMessage responseMessage7 = voteAgendaElementRoute.voteAgenda(eventId, agendaElementId, userId, "3.4");
        ResponseMessage responseMessage8 = voteAgendaElementRoute.voteAgenda(eventId, agendaElementId, userId, "4.5");

        ResponseMessage responseMessage9 = voteAgendaElementRoute.voteAgenda(eventId, agendaElementId, userId, "5");
        ResponseMessage responseMessage10 = voteAgendaElementRoute.voteAgenda(eventId, agendaElementId, userId2, "4");


        // Then
        assertFalse(responseMessage1.isStatus());
        assertFalse(responseMessage2.isStatus());
        assertFalse(responseMessage3.isStatus());
        assertFalse(responseMessage4.isStatus());
        assertFalse(responseMessage5.isStatus());
        assertFalse(responseMessage6.isStatus());
        assertFalse(responseMessage7.isStatus());
        assertFalse(responseMessage8.isStatus());

        assertTrue(responseMessage9.isStatus());
        assertTrue(responseMessage10.isStatus());

        AgendaElement agendaElement1 = (AgendaElement) responseMessage10.getReturnObject();

        assertEquals(4.5, agendaElement1.getStar(), 0.01);
        assertEquals(2, agendaElement1.getVoteCount());

        ResponseMessage responseMessage11 = voteAgendaElementRoute.voteAgenda(eventId, agendaElementId, userId2, "2");


        assertTrue(responseMessage11.isStatus());

        AgendaElement agendaElement2 = (AgendaElement) responseMessage11.getReturnObject();
        assertEquals(2, agendaElement2.getVoteCount());

        ResponseMessage responseMessage12 = voteAgendaElementRoute.voteAgenda(eventId, agendaElementId, userId3, "1");

        assertTrue(responseMessage12.isStatus());

        AgendaElement agendaElement3 = (AgendaElement) responseMessage12.getReturnObject();
        assertEquals(3, agendaElement3.getVoteCount());

    }

}
