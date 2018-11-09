package javaday.istanbul.sliconf.micro.steps.other;

import cucumber.api.java.tr.Diyelimki;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.event.controller.DeleteEventRoute;
import javaday.istanbul.sliconf.micro.user.controller.LoginUserRoute;
import javaday.istanbul.sliconf.micro.user.controller.UpdateUserRoute;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.event.service.EventRepositoryService;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Ignore
public class DeleteEventTest extends SpringBootTestConfig { // NOSONAR

    @Autowired
    UserRepositoryService userRepositoryService;

    @Autowired
    EventRepositoryService eventRepositoryService;

    @Autowired
    UpdateUserRoute updateUserRoute;

    @Autowired
    LoginUserRoute loginUserRoute;

    @Autowired
    DeleteEventRoute deleteEventRoute;

    @Diyelimki("^Kullanici kendine ait eventi silmek istiyor$")
    public void kullaniciKendineAitEventiSilmekIstiyor() throws Throwable {

        // Given
        User user = new User();
        user.setEmail("osmanEventDelete@baykal.com");
        user.setUsername("osmanEventDelete");
        user.setPassword("123123123");

        ResponseMessage responseMessageUser = userRepositoryService.saveUser(user);

        String userId = ((User) responseMessageUser.getReturnObject()).getId();

        Event event = new Event();
        event.setKey("KJK6");
        event.setExecutiveUser(userId);

        eventRepositoryService.save(event);

        // When
        ResponseMessage responseMessage = deleteEventRoute.deleteEvent(event.getKey(), userId);

        Event event1 = new Event();
        event.setKey("KJK5");
        event.setExecutiveUser(userId);

        eventRepositoryService.save(event1);

        ResponseMessage responseMessage1 = deleteEventRoute.deleteEvent(event1.getKey(), "userId1213");

        ResponseMessage responseMessage2 = deleteEventRoute.deleteEvent("", "");
        ResponseMessage responseMessage3 = deleteEventRoute.deleteEvent(null, userId);
        ResponseMessage responseMessage4 = deleteEventRoute.deleteEvent(event1.getKey(), null);

        // Then
        assertTrue(responseMessage.isStatus());
        assertFalse(responseMessage1.isStatus());
        assertFalse(responseMessage2.isStatus());
        assertFalse(responseMessage3.isStatus());
        assertFalse(responseMessage4.isStatus());

    }

}
