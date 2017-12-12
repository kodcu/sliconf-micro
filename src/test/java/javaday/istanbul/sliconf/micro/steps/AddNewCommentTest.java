package javaday.istanbul.sliconf.micro.steps;

import cucumber.api.java.tr.Diyelimki;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.builder.EventBuilder;
import javaday.istanbul.sliconf.micro.controller.event.comment.AddNewCommentRoute;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.*;
import javaday.istanbul.sliconf.micro.model.event.agenda.AgendaElement;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.comment.CommentRepositoryService;
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
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@ContextConfiguration(classes = {CucumberConfiguration.class})
@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class AddNewCommentTest {

    @Autowired
    UserRepositoryService userRepositoryService;

    @Autowired
    EventRepositoryService eventRepositoryService;

    @Autowired
    CommentRepositoryService commentRepositoryService;

    @Autowired
    AddNewCommentRoute addNewCommentRoute;


    private List<Speaker> speakers;
    private List<AgendaElement> agendaElements;
    private List<Room> rooms;
    private List<Floor> floors;

    @Diyelimki("^Kullanici konusmaya yeni bir yorum yazmak istiyor$")
    public void kullaniciKonusmayaYeniBirYorumYazmakIstiyor() throws Throwable {
        // Given
        User user = new User();
        user.setUsername("commentUser1");
        user.setEmail("commentuser1@sliconf.com");
        user.setPassword("123123123");
        ResponseMessage savedUserMessage = userRepositoryService.saveUser(user);

        assertTrue(savedUserMessage.isStatus());

        String userId = ((User) savedUserMessage.getReturnObject()).getId();

        Event event = new EventBuilder().setName("Comment Event")
                .setExecutiveUser(userId)
                .setDate(LocalDateTime.now().plusMonths(2)).build();

        EventSpecs.generateKanbanNumber(event, eventRepositoryService);

        generateFields();

        event.setStatus(true);
        event.setFloorPlan(floors);
        event.setRooms(rooms);
        event.setSpeakers(speakers);
        event.setAgenda(agendaElements);

        ResponseMessage eventSaveMessage = eventRepositoryService.save(event);
        String eventId = ((Event) eventSaveMessage.getReturnObject()).getId();

        Comment comment1 = new Comment();
        comment1.setUserId(userId);
        comment1.setApproved(Comment.APPROVED.PENDING);
        comment1.setCommentValue("This talk is awesome");
        comment1.setEventId(eventId);
        comment1.setId("comment-1");
        comment1.setSessionId("agenda-element-1");
        comment1.setTime(LocalDateTime.now());

        Comment comment2 = new Comment();
        comment2.setUserId("no user id");
        comment2.setApproved(Comment.APPROVED.PENDING);
        comment2.setCommentValue("This talk is awesome");
        comment2.setEventId(eventId);
        comment2.setId("comment-2");
        comment2.setSessionId("agenda-element-1");
        comment2.setTime(LocalDateTime.now());

        Comment comment3 = new Comment();
        comment3.setUserId(userId);
        comment3.setApproved(Comment.APPROVED.PENDING);
        comment3.setCommentValue("This talk is awesome");
        comment3.setEventId("noeventid");
        comment3.setId("comment-3");
        comment3.setSessionId("agenda-element-1");
        comment3.setTime(LocalDateTime.now());

        Comment comment4 = new Comment();
        comment4.setUserId(userId);
        comment4.setApproved(Comment.APPROVED.PENDING);
        comment4.setCommentValue("This talk is awesome");
        comment4.setEventId(eventId);
        comment4.setId("comment-4");
        comment4.setSessionId("agenda-element-null");
        comment4.setTime(LocalDateTime.now());

        Comment comment5 = new Comment();
        comment5.setUserId(userId);
        comment5.setApproved(Comment.APPROVED.PENDING);
        comment5.setCommentValue("This talk is awesome");
        comment5.setEventId(eventId);
        comment5.setId("comment-5");
        comment5.setSessionId("agenda-element-1");
        comment5.setTime(LocalDateTime.now().minusDays(7));

        // When
        ResponseMessage commentAddMessage1 = addNewCommentRoute.addNewComment(comment1);

        ResponseMessage commentAddMessage2 = addNewCommentRoute.addNewComment(comment2);
        ResponseMessage commentAddMessage3 = addNewCommentRoute.addNewComment(comment3);
        ResponseMessage commentAddMessage4 = addNewCommentRoute.addNewComment(comment4);
        ResponseMessage commentAddMessage5 = addNewCommentRoute.addNewComment(comment5);


        // Then
        assertTrue(commentAddMessage1.isStatus());

        assertFalse(commentAddMessage2.isStatus());
        assertFalse(commentAddMessage3.isStatus());
        assertFalse(commentAddMessage4.isStatus());
        assertFalse(commentAddMessage5.isStatus());
    }

    private void generateFields() {
        generateFloors();
        generateRooms();
        generateSpeakers();
        generateAgenda();
    }

    private void generateFloors() {
        floors = new ArrayList<>();

        Floor floor = new Floor();
        floor.setId("floor123");
        floor.setName("Floor 1");

        floors.add(floor);
    }

    private void generateRooms() {
        rooms = new ArrayList<>();

        Room room = new Room();
        room.setId("room123");
        room.setFloor("floor123");
        room.setLabel("Room 1");

        rooms.add(room);
    }

    private void generateSpeakers() {
        speakers = new ArrayList<>();

        Speaker speaker = new Speaker();
        speaker.setId("speaker123");
        speaker.setName("Test Speaker");
        speaker.setTopics(new ArrayList<>());

        speakers.add(speaker);
    }

    private void generateAgenda() {
        agendaElements = new ArrayList<>();

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
