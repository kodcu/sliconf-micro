package javaday.istanbul.sliconf.micro.steps;

import cucumber.api.java.tr.Diyelimki;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.builder.EventBuilder;
import javaday.istanbul.sliconf.micro.controller.event.comment.AddNewCommentRoute;
import javaday.istanbul.sliconf.micro.controller.login.LoginUserRoute;
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
public class LoginUserTest {// NOSONAR

    @Autowired
    UserRepositoryService userRepositoryService;

    @Autowired
    LoginUserRoute loginUserRoute;


    @Diyelimki("^Kullanici Login Oluyor$")
    public void kullaniciLoginOluyor() throws Throwable {
        // Given
        User user = new User();
        user.setUsername("loginUser1");
        user.setEmail("loginUser1@sliconf.com");
        user.setPassword("123123123");
        ResponseMessage savedUserMessage = userRepositoryService.saveUser(user);

        assertTrue(savedUserMessage.isStatus());

        User user1 = new User();
        user1.setUsername("loginUser1");
        user1.setEmail("loginUser1@sliconf.com");
        user1.setPassword("123123123");

        User user2 = new User();
        user2.setUsername("loginUser1@sliconf.com");
        user2.setPassword("123123123");

        User user3 = new User();
        user3.setUsername("loginUser1@sliconf.com");
        user3.setPassword("1231231231");

        User user4 = new User();
        user4.setUsername("loginUser11@sliconf.com");
        user4.setPassword("1231231231");

        User user5 = new User();
        user5.setUsername("loginUser1");
        user5.setPassword("1231231231");

        User user6 = new User();
        user6.setUsername("");
        user6.setPassword("123123123");

        User user7 = new User();
        user7.setUsername(null);
        user7.setPassword("123123123");

        User user8 = new User();
        user8.setUsername("loginUser1");
        user8.setPassword("");

        User user9 = new User();
        user9.setUsername("loginUser1");
        user9.setPassword(null);

        // When
        ResponseMessage loginMessage1 = loginUserRoute.loginUser(user1);
        ResponseMessage loginMessage2 = loginUserRoute.loginUser(user2);

        ResponseMessage loginMessage3 = loginUserRoute.loginUser(user3);
        ResponseMessage loginMessage4 = loginUserRoute.loginUser(user4);
        ResponseMessage loginMessage5 = loginUserRoute.loginUser(user5);
        ResponseMessage loginMessage6 = loginUserRoute.loginUser(user6);
        ResponseMessage loginMessage7 = loginUserRoute.loginUser(user7);
        ResponseMessage loginMessage8 = loginUserRoute.loginUser(user8);
        ResponseMessage loginMessage9 = loginUserRoute.loginUser(user9);



        // Then
        assertTrue(loginMessage1.isStatus());
        assertTrue(loginMessage2.isStatus());

        assertFalse(loginMessage3.isStatus());
        assertFalse(loginMessage4.isStatus());
        assertFalse(loginMessage5.isStatus());
        assertFalse(loginMessage6.isStatus());
        assertFalse(loginMessage7.isStatus());
        assertFalse(loginMessage8.isStatus());
        assertFalse(loginMessage9.isStatus());
    }


}
