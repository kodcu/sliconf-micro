package javaday.istanbul.sliconf.micro.steps;

import cucumber.api.java.tr.Diyelimki;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.controller.event.DeleteEventRoute;
import javaday.istanbul.sliconf.micro.controller.login.LoginUserRoute;
import javaday.istanbul.sliconf.micro.controller.login.UpdateUserRoute;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@ContextConfiguration(classes = {CucumberConfiguration.class})
@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class DeleteEventTest {// NOSONAR

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
