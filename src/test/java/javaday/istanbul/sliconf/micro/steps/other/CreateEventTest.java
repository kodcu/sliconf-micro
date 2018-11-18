package javaday.istanbul.sliconf.micro.steps.other;

import cucumber.api.java.tr.Diyelimki;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.event.EventBuilder;
import javaday.istanbul.sliconf.micro.event.EventSpecs;
import javaday.istanbul.sliconf.micro.event.controller.CreateEventRoute;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.event.model.LifeCycleState;
import javaday.istanbul.sliconf.micro.event.service.EventRepositoryTestService;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Ignore
public class CreateEventTest extends SpringBootTestConfig { // NOSONAR

    @Autowired
    UserRepositoryService userRepositoryService;

    @Autowired
    EventRepositoryTestService eventRepositoryService;

    @Autowired
    CreateEventRoute createEventRoute;


    @Diyelimki("^Yeni bir event olusturuluyor$")
    public void yeniBirEventOlusturuluyor() throws Throwable {
        // Given
        User user1 = new User();
        user1.setUsername("createEventUser86");
        user1.setEmail("createEventUser86@sliconf.com");
        user1.setPassword("123123123");
        ResponseMessage savedUserMessage1 = userRepositoryService.saveUser(user1);

        assertTrue(savedUserMessage1.isStatus());

        String userId1 = ((User) savedUserMessage1.getReturnObject()).getId();

        User user2 = new User();
        user2.setUsername("createEventUser86");
        user2.setEmail("createEventUser86@sliconf.com");
        user2.setPassword("123123123");
        ResponseMessage savedUserMessage2 = userRepositoryService.saveUser(user2);

        assertTrue(savedUserMessage2.isStatus());

        String userId2 = ((User) savedUserMessage2.getReturnObject()).getId();

        // Everything is ok
        Event event1 = new EventBuilder()
                .setName("Create Event 86")
                .setExecutiveUser(userId1)
                .setDate(LocalDateTime.now().plusWeeks(2))
                .setEndDate(LocalDateTime.now().plusWeeks(3))
                .build();

        // invalid Date
        Event event2 = new EventBuilder()
                .setName("Create Event 87")
                .setExecutiveUser(userId2)
                .setDate(LocalDateTime.now().minusMonths(2)).build();

        // already used Name
        Event event3 = new EventBuilder()
                .setName("Create Event 86")
                .setExecutiveUser(userId2)
                .setDate(LocalDateTime.now().plusWeeks(2))
                .setEndDate(LocalDateTime.now().plusWeeks(3))
                .build();

        Event updateEvent1 = new EventBuilder().setName("Create Event saved")
                .setExecutiveUser(userId1)
                .setDate(LocalDateTime.now().plusWeeks(2))
                .setEndDate(LocalDateTime.now().plusWeeks(3))
                .build();

        EventSpecs.generateKanbanNumber(updateEvent1, eventRepositoryService);
        LifeCycleState lifeCycleState = new LifeCycleState();
        lifeCycleState.setEventStatuses(new ArrayList<>());
        updateEvent1.setLifeCycleState(lifeCycleState);


        ResponseMessage eventSaveMessage = eventRepositoryService.save(updateEvent1);

        Event updateEvent = (Event) eventSaveMessage.getReturnObject();

        updateEvent.setDescription("Test 123");


        Event event4 = new EventBuilder()
                .setName("Create Event 89")
                .setExecutiveUser(userId1)
                .setKey("")
                .setDate(LocalDateTime.now().plusWeeks(2))
                .setEndDate(LocalDateTime.now().plusWeeks(3))
                .build();

        Event event5 = new EventBuilder()
                .setName("Cre")
                .setExecutiveUser(userId1)
                .setDate(LocalDateTime.now().plusWeeks(2))
                .setEndDate(LocalDateTime.now().plusWeeks(3))
                .build();

        Event updateEvent2 = new EventBuilder().setName("Update event 2")
                .setExecutiveUser(userId1)
                .setDate(LocalDateTime.now().plusWeeks(2))
                .setEndDate(LocalDateTime.now().plusWeeks(3))
                .build();

        EventSpecs.generateKanbanNumber(updateEvent2, eventRepositoryService);

        ResponseMessage eventSaveMessage2 = eventRepositoryService.save(updateEvent2);

        Event updateEventSaved2 = (Event) eventSaveMessage2.getReturnObject();

        updateEventSaved2.setName("Tes");

        // When
        ResponseMessage createEventMessage1 = createEventRoute.processEvent(event1, userId1);
        ResponseMessage createEventMessage12 = createEventRoute.processEvent(event4, userId1);

        ResponseMessage createEventMessage2 = createEventRoute.processEvent(event2, userId2);
        ResponseMessage createEventMessage3 = createEventRoute.processEvent(event3, userId2);

        // Wrong executive user
        ResponseMessage createEventMessage4 = createEventRoute.processEvent(event2, userId1);
        // null userId
        ResponseMessage createEventMessage5 = createEventRoute.processEvent(event2, null);
        // empty userId
        ResponseMessage createEventMessage6 = createEventRoute.processEvent(event2, "");
        // null event
        ResponseMessage createEventMessage7 = createEventRoute.processEvent(null, userId1);

        // wrong userId
        ResponseMessage createEventMessage15 = createEventRoute.processEvent(event2, "asdasd-adad123");

        // update Event everything is ok
        ResponseMessage createEventMessage8 = createEventRoute.processEvent(updateEvent, userId1);

        // wrong userId
        ResponseMessage createEventMessage9 = createEventRoute.processEvent(updateEvent, userId2);

        // null userId
        ResponseMessage createEventMessage10 = createEventRoute.processEvent(updateEvent, null);

        // empty userId
        ResponseMessage createEventMessage11 = createEventRoute.processEvent(updateEvent, "");

        // Name length short
        ResponseMessage createEventMessage13 = createEventRoute.processEvent(event5, userId1);
        ResponseMessage createEventMessage14 = createEventRoute.processEvent(updateEventSaved2, userId1);

        // wrong userId
        ResponseMessage createEventMessage16 = createEventRoute.processEvent(updateEvent, "asd-13asd");

        // Wrong date
        updateEvent.setStartDate(LocalDateTime.now().minusMonths(2));
        ResponseMessage createEventMessage17 = createEventRoute.processEvent(updateEvent, "asd-13asd");

        updateEvent.setStartDate(LocalDateTime.now().plusWeeks(2));


        // Then
        assertTrue(createEventMessage1.isStatus());
        assertTrue(createEventMessage8.isStatus());
        assertTrue(createEventMessage12.isStatus());

        assertFalse(createEventMessage2.isStatus());
        assertFalse(createEventMessage3.isStatus());
        assertFalse(createEventMessage4.isStatus());
        assertFalse(createEventMessage5.isStatus());
        assertFalse(createEventMessage6.isStatus());
        assertFalse(createEventMessage7.isStatus());

        assertFalse(createEventMessage9.isStatus());
        assertFalse(createEventMessage10.isStatus());
        assertFalse(createEventMessage11.isStatus());
        assertFalse(createEventMessage13.isStatus());
        assertFalse(createEventMessage14.isStatus());
        assertFalse(createEventMessage15.isStatus());
        assertFalse(createEventMessage16.isStatus());
        assertFalse(createEventMessage17.isStatus());

    }


}
