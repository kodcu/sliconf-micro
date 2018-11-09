package javaday.istanbul.sliconf.micro.steps.admin;

import cucumber.api.java.tr.Diyelimki;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.admin.controller.AdminListEventsRoute;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.security.TokenAuthenticationService;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import javaday.istanbul.sliconf.micro.util.Constants;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import static org.junit.Assert.*;

@Ignore
public class AdminListEventsTest extends SpringBootTestConfig { // NOSONAR

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Autowired
    private AdminListEventsRoute adminListEventsRoute;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Diyelimki("^Yonetici sistemdeki etkinlikleri listeliyor$")
    public void yoneticiSistemdekiEtkinlikleriListeliyor() throws Throwable {
        // Given
        User adminUser = new User();
        adminUser.setUsername("adminUserListEvent");
        adminUser.setEmail("adminUserListEvent@sliconf.com");
        adminUser.setPassword("123123123");
        adminUser.setRole(Constants.ROLE_ADMIN);

        ResponseMessage userSaveResponseMessage = userRepositoryService.saveUser(adminUser);

        assertTrue(userSaveResponseMessage.getMessage(), userSaveResponseMessage.isStatus());

        Authentication authentication1 = tokenAuthenticationService.generateAuthentication(adminUser.getUsername(), adminUser.getRole(), adminUser);

        Authentication authentication2 = tokenAuthenticationService.generateAuthentication(adminUser.getUsername(), "ROLE_USER", adminUser);

        // When
        ResponseMessage responseMessage1 = adminListEventsRoute.getEvents(authentication1);
        ResponseMessage responseMessage2 = adminListEventsRoute.getEvents(authentication2);
        ResponseMessage responseMessage3 = adminListEventsRoute.getEvents(null);

        // Then
        assertTrue(responseMessage1.isStatus());
        assertNotNull(responseMessage1.getReturnObject());

        assertFalse(responseMessage2.isStatus());
        assertFalse(responseMessage3.isStatus());
    }
}
