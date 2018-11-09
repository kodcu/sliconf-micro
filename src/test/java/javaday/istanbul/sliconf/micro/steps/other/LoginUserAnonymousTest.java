package javaday.istanbul.sliconf.micro.steps.other;

import cucumber.api.java.tr.Diyelimki;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.user.controller.LoginUserAnonymousRoute;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Ignore
public class LoginUserAnonymousTest extends SpringBootTestConfig { // NOSONAR

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
