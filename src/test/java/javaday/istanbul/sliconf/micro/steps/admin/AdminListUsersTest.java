package javaday.istanbul.sliconf.micro.steps.admin;

import cucumber.api.java.tr.Diyelimki;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.controller.admin.AdminListUsersRoute;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.security.TokenAuthenticationService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.util.Constants;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import static org.junit.Assert.*;

@Ignore
public class AdminListUsersTest extends SpringBootTestConfig { // NOSONAR

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Autowired
    private AdminListUsersRoute adminListUsersRoute;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Diyelimki("^Yonetici sistemdeki kullanicilari listeliyor$")
    public void yoneticiSistemdekiKullanicilariListeliyor() throws Throwable {
        // Given
        User adminUser = new User();
        adminUser.setUsername("adminUserListUser");
        adminUser.setEmail("adminUserListUser@sliconf.com");
        adminUser.setPassword("123123123");
        adminUser.setRole(Constants.ROLE_ADMIN);

        ResponseMessage userSaveResponseMessage = userRepositoryService.saveUser(adminUser);

        assertTrue(userSaveResponseMessage.getMessage(), userSaveResponseMessage.isStatus());

        Authentication authentication1 = tokenAuthenticationService.generateAuthentication(adminUser.getUsername(), adminUser.getRole(), adminUser);

        Authentication authentication2 = tokenAuthenticationService.generateAuthentication(adminUser.getUsername(), "ROLE_USER", adminUser);

        // When
        ResponseMessage responseMessage1 = adminListUsersRoute.getUsers(authentication1);
        ResponseMessage responseMessage2 = adminListUsersRoute.getUsers(authentication2);
        ResponseMessage responseMessage3 = adminListUsersRoute.getUsers(null);

        // Then
        assertTrue(responseMessage1.isStatus());
        assertNotNull(responseMessage1.getReturnObject());

        assertFalse(responseMessage2.isStatus());
        assertFalse(responseMessage3.isStatus());
    }
}
