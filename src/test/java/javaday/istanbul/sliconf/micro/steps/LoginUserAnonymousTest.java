package javaday.istanbul.sliconf.micro.steps;

import cucumber.api.java.tr.Diyelimki;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.builder.EventBuilder;
import javaday.istanbul.sliconf.micro.controller.event.comment.AddNewCommentRoute;
import javaday.istanbul.sliconf.micro.controller.login.LoginUserAnonymousRoute;
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
public class LoginUserAnonymousTest {// NOSONAR

    @Autowired
    UserRepositoryService userRepositoryService;

    @Autowired
    LoginUserAnonymousRoute loginUserAnonymousRoute;

    @Diyelimki("^Anonim kullanici login$")
    public void anonimKullaniciLogin() throws Throwable {
        // Given
        User user = new User();
        user.setUsername("commentUserAnonymous");
        user.setEmail("commentUserAnonymous@sliconf.com");
        user.setPassword("123123123");
        user.setAnonymous(true);
        user.setDeviceId("deviceId-Anonymous-1");

        ResponseMessage savedUserMessage = userRepositoryService.saveUser(user);

        assertTrue(savedUserMessage.isStatus());

        ResponseMessage loginUserMessage1 = loginUserAnonymousRoute.getUserWithDeviceId("deviceId-Anonymous-1");
        ResponseMessage loginUserMessage2 = loginUserAnonymousRoute.getUserWithDeviceId("deviceId-Anonymous-2");

        // Then
        assertTrue(loginUserMessage1.isStatus());
        assertFalse(loginUserMessage2.isStatus());
    }


}
