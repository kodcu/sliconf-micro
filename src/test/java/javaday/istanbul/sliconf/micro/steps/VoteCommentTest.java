package javaday.istanbul.sliconf.micro.steps;

import cucumber.api.java.tr.Diyelimki;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.builder.EventBuilder;
import javaday.istanbul.sliconf.micro.controller.event.comment.AddNewCommentRoute;
import javaday.istanbul.sliconf.micro.controller.event.comment.VoteCommentRoute;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.*;
import javaday.istanbul.sliconf.micro.model.event.agenda.AgendaElement;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.comment.CommentRepositoryService;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.specs.EventSpecs;
import javaday.istanbul.sliconf.micro.util.TestUtil;
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
public class VoteCommentTest {

    @Autowired
    UserRepositoryService userRepositoryService;

    @Autowired
    EventRepositoryService eventRepositoryService;

    @Autowired
    AddNewCommentRoute addNewCommentRoute;

    @Autowired
    CommentRepositoryService commentRepositoryService;

    @Autowired
    VoteCommentRoute voteCommentRoute;

    private List<Speaker> speakers = new ArrayList<>();
    private List<AgendaElement> agendaElements = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private List<Floor> floors = new ArrayList<>();

    @Diyelimki("^Yazilmis bir comment e oy veriliyor$")
    public void yazilmisBirCommentEOyVeriliyor() throws Throwable {

        int dislike = -1;
        int notr = 0;
        int like = 1;

        User user = new User();
        user.setUsername("commentUser3");
        user.setEmail("commentuser3@sliconf.com");
        user.setPassword("123123123");
        ResponseMessage savedUserMessage = userRepositoryService.saveUser(user);

        assertTrue(savedUserMessage.isStatus());

        String userId = ((User) savedUserMessage.getReturnObject()).getId();

        User user2 = new User();
        user2.setUsername("commentUser3");
        user2.setEmail("commentuser3@sliconf.com");
        user2.setPassword("123123123");
        ResponseMessage savedUserMessage2 = userRepositoryService.saveUser(user2);

        assertTrue(savedUserMessage2.isStatus());

        String userId2 = ((User) savedUserMessage2.getReturnObject()).getId();

        User user3 = new User();
        user3.setUsername("commentUser3");
        user3.setEmail("commentuser3@sliconf.com");
        user3.setPassword("123123123");
        ResponseMessage savedUserMessage3 = userRepositoryService.saveUser(user3);

        assertTrue(savedUserMessage3.isStatus());

        String userId3 = ((User) savedUserMessage3.getReturnObject()).getId();

        Event event = new EventBuilder().setName("Comment Event 2")
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
        comment1.setApproved("approved");

        comment1.setLike(5);
        comment1.setDislike(2);

        Comment comment2 = new Comment();
        comment2.setUserId(userId2);
        comment2.setCommentValue("This talk is awesome 2");
        comment2.setEventId(eventId);
        comment2.setId("comment-2");
        comment2.setSessionId("agenda-element-1");
        comment2.setTime(LocalDateTime.now());
        comment2.setApproved("approved");

        Comment comment3 = new Comment();
        comment3.setUserId(userId2);
        comment3.setCommentValue("This talk is awesome 3");
        comment3.setEventId(eventId);
        comment3.setId("comment-3");
        comment3.setSessionId("agenda-element-1");
        comment3.setTime(LocalDateTime.now());
        comment3.setApproved("pending");

        Comment comment4 = new Comment();
        comment4.setUserId(userId2);
        comment4.setCommentValue("This talk is awesome 4");
        comment4.setEventId(eventId);
        comment4.setId("comment-4");
        comment4.setSessionId("agenda-element-1");
        comment4.setTime(LocalDateTime.now());
        comment4.setApproved("denied");


        // - Kullanici kendi yorumunu like ya da dislike atamaz
        // - Kullanici like attigi yoruma bir daha like atamaz
        // - Kullanici dislike attigi bir yoruma bir daha dislike atamaz
        // - Kullanicinin bir yorumda en fazla bir oyu olabilir dislike ya da like ayni anda olamaz
        // - Kullanici onaylanmis bir yoruma oy verebilir. Eger yorum onayli degilse oy veremez

        // - Eger kullanici like verirse, likes listesine eklenir
        // - Eger kullanici dislike verirse dislikes listesine eklenir
        // - Eger kullanici likes listesinde iken dislike verirse likes listesinden cikarilip, dislikes listesine eklenir
        // - Eger kullanici dislikes listesinde iken like verirse dislikes listesinden cikartilip, likes listesine eklenir
        // - Eger kullanici like verirse like alani bir artar
        // - Eger kullanici dislike verirse dislike alani bir artar
        // - Eger kullanici daha once like verdiyse ve dislike olarak degistiriyorsa, like bir azaltilip dislike bir artar
        // - Eger kullanici daha once dislike verdiyse ve like olarak degistiriyorsa, dislike bir azaltilip like bir artar
        // - Eger kullanici daha once bir like ya da dislike verdiyse ve yeni oyu notr ise verilmis olan like ya da dislike bir azaltilir
        // - Eger kullanici daha once likes ya da dislikes listesinde iken notr oy verirse likes ya da dislikes listesinden cikarilir

        // When
        commentRepositoryService.save(comment1);
        commentRepositoryService.save(comment2);
        commentRepositoryService.save(comment3);

        // - Kullanici kendi yorumunu like ya da dislike atamaz
        ResponseMessage voteCommentMessage1 = voteCommentRoute.voteComment(comment1.getId(), userId, like); // false , like=5, dislike=2
        ResponseMessage voteCommentMessage2 = voteCommentRoute.voteComment(comment1.getId(), userId, dislike); // false, like=5,dislike=2

        // - Kullanici like attigi yoruma bir daha like atamaz
        // - Kullanici dislike attigi bir yoruma bir daha dislike atamaz
        ResponseMessage voteCommentMessage3 = voteCommentRoute.voteComment(comment1.getId(), userId2, like); // false
        ResponseMessage voteCommentMessage4 = voteCommentRoute.voteComment(comment1.getId(), userId2, dislike); // false

        // - Kullanici onaylanmis bir yoruma oy verebilir. Eger yorum onayli degilse oy veremez
        ResponseMessage voteCommentMessage5 = voteCommentRoute.voteComment(comment3.getId(), userId, like); // false


        // - Eger kullanici like verirse, likes listesine eklenir
        ResponseMessage voteCommentMessage6 = voteCommentRoute.voteComment(comment1.getId(), userId3, like); // false

        // - Eger kullanici dislike verirse dislikes listesine eklenir
        ResponseMessage voteCommentMessage7 = voteCommentRoute.voteComment(comment2.getId(), userId3, dislike); // false

        // - Eger kullanici likes listesinde iken dislike verirse likes listesinden cikarilip, dislikes listesine eklenir
        ResponseMessage voteCommentMessage8 = voteCommentRoute.voteComment(comment1.getId(), userId3, dislike); // false

        // - Eger kullanici dislikes listesinde iken like verirse dislikes listesinden cikartilip, likes listesine eklenir
        ResponseMessage voteCommentMessage9 = voteCommentRoute.voteComment(comment2.getId(), userId3, like); // false

        ResponseMessage voteCommentMessage10 = voteCommentRoute.voteComment(comment2.getId(), userId3, notr); // false
        ResponseMessage voteCommentMessage11 = voteCommentRoute.voteComment(comment2.getId(), userId2, notr); // false
        ResponseMessage voteCommentMessage12 = voteCommentRoute.voteComment(comment2.getId(), userId, notr); // false

        // - Eger kullanici like verirse like alani bir artar

        // ResponseMessage voteCommentMessage10 = voteCommentRoute.voteComment(comment2.getId(), userId3, dislike); // false

        // - Eger kullanici dislike verirse dislike alani bir artar
        // - Eger kullanici daha once like verdiyse ve dislike olarak degistiriyorsa, like bir azaltilip dislike bir artar
        // - Eger kullanici daha once dislike verdiyse ve like olarak degistiriyorsa, dislike bir azaltilip like bir artar
        // - Eger kullanici daha once bir like ya da dislike verdiyse ve yeni oyu notr ise verilmis olan like ya da dislike bir azaltilir
        // - Eger kullanici daha once likes ya da dislikes listesinde iken notr oy verirse likes ya da dislikes listesinden cikarilir


        // Then
        assertFalse(voteCommentMessage1.isStatus());

    }

}
