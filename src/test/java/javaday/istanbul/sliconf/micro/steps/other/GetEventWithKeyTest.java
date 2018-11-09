package javaday.istanbul.sliconf.micro.steps.other;


import cucumber.api.java.tr.Diyelimki;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.event.EventBuilder;
import javaday.istanbul.sliconf.micro.event.controller.GetEventWithKeyRoute;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.event.service.EventRepositoryService;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import javaday.istanbul.sliconf.micro.event.EventSpecs;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Ignore
public class GetEventWithKeyTest extends SpringBootTestConfig { // NOSONAR

    @Autowired
    UserRepositoryService userRepositoryService;

    @Autowired
    EventRepositoryService eventRepositoryService;

    @Autowired
    GetEventWithKeyRoute getEventWithKeyRoute;


    @Diyelimki("^Event key ile getiriliyor")
    public void eventKeyIleGetiriliyor() throws Throwable {
        // Given
        User user1 = new User();
        user1.setUsername("getEventWithKeyUser1");
        user1.setEmail("getEventWithKeyUser1@sliconf.com");
        user1.setPassword("123123123");

        ResponseMessage savedUserMessage = userRepositoryService.saveUser(user1);

        assertTrue(savedUserMessage.isStatus());

        String userId1 = ((User) savedUserMessage.getReturnObject()).getId();

        User user2 = new User();
        user2.setUsername("getEventWithKeyUser2");
        user2.setEmail("getEventWithKeyUser2@sliconf.com");
        user2.setPassword("123123123");

        ResponseMessage savedUserMessage2 = userRepositoryService.saveUser(user2);

        assertTrue(savedUserMessage2.isStatus());

        String userId2 = ((User) savedUserMessage2.getReturnObject()).getId();


        Event event1 = new EventBuilder().setName("Get Event With Key Test")
                .setExecutiveUser(userId1)
                .setDate(LocalDateTime.now().plusMonths(2)).build();

        EventSpecs.generateKanbanNumber(event1, eventRepositoryService);
        event1.setStatus(true);

        ResponseMessage eventSaveMessage = eventRepositoryService.save(event1);
        String eventKey = ((Event) eventSaveMessage.getReturnObject()).getKey();


        // When

        // True
        ResponseMessage saveGetEventResponseMessage1 = getEventWithKeyRoute.getEventWithKey(eventKey, userId1, "true");
        ResponseMessage saveGetEventResponseMessage2 = getEventWithKeyRoute.getEventWithKey(eventKey, userId1, "false");
        ResponseMessage saveGetEventResponseMessage3 = getEventWithKeyRoute.getEventWithKey(eventKey, userId1, "");
        ResponseMessage saveGetEventResponseMessage4 = getEventWithKeyRoute.getEventWithKey(eventKey, userId1, null);
        ResponseMessage saveGetEventResponseMessage5 = getEventWithKeyRoute.getEventWithKey(eventKey, userId1, "asdasd");

        ResponseMessage saveGetEventResponseMessage6 = getEventWithKeyRoute.getEventWithKey(eventKey, userId2, "true");
        ResponseMessage saveGetEventResponseMessage7 = getEventWithKeyRoute.getEventWithKey(eventKey, userId2, "false");
        ResponseMessage saveGetEventResponseMessage8 = getEventWithKeyRoute.getEventWithKey(eventKey, userId2, "");
        ResponseMessage saveGetEventResponseMessage9 = getEventWithKeyRoute.getEventWithKey(eventKey, userId2, null);
        ResponseMessage saveGetEventResponseMessage10 = getEventWithKeyRoute.getEventWithKey(eventKey, userId2, "asdasd");
        ResponseMessage saveGetEventResponseMessage13 = getEventWithKeyRoute.getEventWithKey(eventKey, "", "asdasd");

        // False
        ResponseMessage saveGetEventResponseMessage11 = getEventWithKeyRoute.getEventWithKey("", userId2, "asdasd");
        ResponseMessage saveGetEventResponseMessage12 = getEventWithKeyRoute.getEventWithKey(null, userId2, "asdasd");
        ResponseMessage saveGetEventResponseMessage14 = getEventWithKeyRoute.getEventWithKey(eventKey, null, "asdasd");
        ResponseMessage saveGetEventResponseMessage15 = getEventWithKeyRoute.getEventWithKey(eventKey, "asddr2423f", "asdasd");
        ResponseMessage saveGetEventResponseMessage16 = getEventWithKeyRoute.getEventWithKey("44SDF", userId2, "asdasd");


        // Then
        assertTrue(saveGetEventResponseMessage1.isStatus());
        assertTrue(saveGetEventResponseMessage2.isStatus());
        assertTrue(saveGetEventResponseMessage3.isStatus());
        assertTrue(saveGetEventResponseMessage4.isStatus());
        assertTrue(saveGetEventResponseMessage5.isStatus());
        assertTrue(saveGetEventResponseMessage6.isStatus());
        assertTrue(saveGetEventResponseMessage7.isStatus());
        assertTrue(saveGetEventResponseMessage8.isStatus());
        assertTrue(saveGetEventResponseMessage9.isStatus());
        assertTrue(saveGetEventResponseMessage10.isStatus());
        assertTrue(saveGetEventResponseMessage13.isStatus());
        assertTrue(saveGetEventResponseMessage14.isStatus());
        assertTrue(saveGetEventResponseMessage15.isStatus());


        assertFalse(saveGetEventResponseMessage11.isStatus());
        assertFalse(saveGetEventResponseMessage12.isStatus());
        assertFalse(saveGetEventResponseMessage16.isStatus());
    }
}
