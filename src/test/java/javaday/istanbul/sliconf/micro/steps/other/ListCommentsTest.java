package javaday.istanbul.sliconf.micro.steps.other;

import cucumber.api.java.tr.Diyelimki;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.builder.EventBuilder;
import javaday.istanbul.sliconf.micro.controller.event.comment.AddNewCommentRoute;
import javaday.istanbul.sliconf.micro.controller.event.comment.ListCommentsRoute;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.*;
import javaday.istanbul.sliconf.micro.model.event.agenda.AgendaElement;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.comment.CommentRepositoryService;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.specs.EventSpecs;
import javaday.istanbul.sliconf.micro.util.TestUtil;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Ignore
public class ListCommentsTest extends SpringBootTestConfig { // NOSONAR

    @Autowired
    UserRepositoryService userRepositoryService;

    @Autowired
    EventRepositoryService eventRepositoryService;

    @Autowired
    CommentRepositoryService commentRepositoryService;

    @Autowired
    AddNewCommentRoute addNewCommentRoute;

    @Autowired
    ListCommentsRoute listCommentsRoute;


    private List<Speaker> speakers = new ArrayList<>();
    private List<AgendaElement> agendaElements = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private List<Floor> floors = new ArrayList<>();

    @Diyelimki("^Kullanici yorumlari listelemek istedi$")
    public void kullaniciYorumlariListelemekIstedi() throws Throwable {
        // Given
        User user = new User();
        user.setUsername("commentUser2");
        user.setEmail("commentuser2@sliconf.com");
        user.setPassword("123123123");
        ResponseMessage savedUserMessage = userRepositoryService.saveUser(user);

        assertTrue(savedUserMessage.isStatus());

        String userId = ((User) savedUserMessage.getReturnObject()).getId();

        Event event = new EventBuilder().setName("Comment Event")
                .setExecutiveUser(userId)
                .setDate(LocalDateTime.now().plusMonths(2)).build();

        EventSpecs.generateKanbanNumber(event, eventRepositoryService);

        TestUtil.generateFields(floors, rooms, speakers, agendaElements);

        event.setStatus(true);
        event.setFloorPlan(floors);
        event.setRooms(rooms);
        event.setSpeakers(speakers);
        event.setAgenda(agendaElements);

        ResponseMessage eventSaveMessage = eventRepositoryService.save(event);
        String eventId = ((Event) eventSaveMessage.getReturnObject()).getId();

        Comment comment1 = new Comment();
        comment1.setUserId(userId);
        comment1.setCommentValue("This talk is awesome");
        comment1.setEventId(eventId);
        comment1.setId("comment-1");
        comment1.setSessionId("agenda-element-1");
        comment1.setTime(LocalDateTime.now());

        Comment comment2 = new Comment();
        comment2.setUserId(userId);
        comment2.setCommentValue("This talk is awesome");
        comment2.setEventId(eventId);
        comment2.setId("comment-2");
        comment2.setSessionId("agenda-element-1");
        comment2.setTime(LocalDateTime.now());

        Comment comment3 = new Comment();
        comment3.setUserId(userId);
        comment3.setCommentValue("This talk is awesome");
        comment3.setEventId(eventId);
        comment3.setId("comment-3");
        comment3.setSessionId("agenda-element-1");
        comment3.setTime(LocalDateTime.now());

        Comment comment4 = new Comment();
        comment4.setUserId(userId);
        comment4.setCommentValue("This talk is awesome");
        comment4.setEventId(eventId);
        comment4.setId("comment-4");
        comment4.setSessionId("agenda-element-1");
        comment4.setTime(LocalDateTime.now());

        Comment comment5 = new Comment();
        comment5.setUserId(userId);
        comment5.setCommentValue("This talk is awesome");
        comment5.setEventId(eventId);
        comment5.setId("comment-5");
        comment5.setSessionId("agenda-element-1");
        comment5.setTime(LocalDateTime.now());

        // When
        ResponseMessage commentAddMessage1 = addNewCommentRoute.addNewComment(comment1);

        ResponseMessage commentAddMessage2 = addNewCommentRoute.addNewComment(comment2);
        ResponseMessage commentAddMessage3 = addNewCommentRoute.addNewComment(comment3);
        ResponseMessage commentAddMessage4 = addNewCommentRoute.addNewComment(comment4);
        ResponseMessage commentAddMessage5 = addNewCommentRoute.addNewComment(comment5);


        // Then
        assertTrue(commentAddMessage1.isStatus());
        assertTrue(commentAddMessage2.isStatus());
        assertTrue(commentAddMessage3.isStatus());
        assertTrue(commentAddMessage4.isStatus());
        assertTrue(commentAddMessage5.isStatus());

        ResponseMessage listMessage1 = listCommentsRoute.listComments(eventId, "agenda-element-1", userId, "pending", "0", null, null); // true
        ResponseMessage listMessage2 = listCommentsRoute.listComments(eventId, "agenda-element-1", "", "pending", "0", null, null); // true
        ResponseMessage listMessage3 = listCommentsRoute.listComments(eventId, "", userId, "", "0", null, null); // false
        ResponseMessage listMessage4 = listCommentsRoute.listComments(eventId, "", "", "", "0", null, null); // false

        ResponseMessage listMessage5 = listCommentsRoute.listComments("", "agenda-element-1", userId, "approved", "0", null, null); // false
        ResponseMessage listMessage6 = listCommentsRoute.listComments("", "agenda-element-1", "", "approved", "0", null, null); // false
        ResponseMessage listMessage7 = listCommentsRoute.listComments("", "", userId, "approved", "0", null, null); // false
        ResponseMessage listMessage8 = listCommentsRoute.listComments("", "", "", "approved", "0", null, null); // false

        assertTrue(listMessage1.isStatus());
        assertTrue(listMessage2.isStatus());
        assertFalse(listMessage3.isStatus());
        assertFalse(listMessage4.isStatus());

        assertFalse(listMessage5.isStatus());
        assertFalse(listMessage6.isStatus());
        assertFalse(listMessage7.isStatus());
        assertFalse(listMessage8.isStatus());

    }
}
