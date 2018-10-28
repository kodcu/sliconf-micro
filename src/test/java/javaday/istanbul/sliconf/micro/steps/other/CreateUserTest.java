package javaday.istanbul.sliconf.micro.steps.other;

import cucumber.api.java.tr.Diyelimki;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.controller.login.CreateUserRoute;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Ignore
public class CreateUserTest extends SpringBootTestConfig { // NOSONAR

    @Autowired
    CreateUserRoute createUserRoute;

    @Diyelimki("^Yeni Kullanici Olusturuluyor$")
    public void yeniKullaniciOlusturuluyor() throws Throwable {
        // Given
        User user = new User();
        user.setUsername("registerUser1");
        user.setEmail("registerUser1@sliconf.com");
        user.setPassword("123123123");

        User user1 = new User();
        user1.setUsername("registerUser2");
        user1.setEmail("registerUser2sliconf.com");
        user1.setPassword("123123123");

        User user2 = new User();
        user2.setUsername("registerUser1@sliconf.com");
        user2.setPassword("123123123");

        User user3 = new User();
        user3.setUsername("registerUser1@sliconf.com");
        user3.setPassword("1231231231");

        User user4 = new User();
        user4.setUsername("registerUser11@sliconf.com");
        user4.setPassword("1231231231");

        User user5 = new User();
        user5.setUsername("registerUser1");
        user5.setPassword("1231231231");

        User user6 = new User();
        user6.setUsername("");
        user6.setPassword("123123123");

        User user7 = new User();
        user7.setUsername(null);
        user7.setPassword("123123123");

        User user8 = new User();
        user8.setUsername("registerUser1");
        user8.setPassword("");

        User user9 = new User();
        user9.setUsername("registerUser1");
        user9.setPassword(null);

        User user11 = new User();
        user11.setUsername("registerUser1");
        user11.setEmail("registerUser1@sliconf.com");
        user11.setPassword("123123123");

        User user12 = new User();
        user12.setUsername("registerUser12");
        user12.setEmail("registerUser1@sliconf.com");
        user12.setPassword("123123123");

        // When
        ResponseMessage registerMessage = createUserRoute.registerUser(user);
        ResponseMessage registerMessage1 = createUserRoute.registerUser(user1);
        ResponseMessage registerMessage2 = createUserRoute.registerUser(user2);
        ResponseMessage registerMessage3 = createUserRoute.registerUser(user3);
        ResponseMessage registerMessage4 = createUserRoute.registerUser(user4);
        ResponseMessage registerMessage5 = createUserRoute.registerUser(user5);
        ResponseMessage registerMessage6 = createUserRoute.registerUser(user6);
        ResponseMessage registerMessage7 = createUserRoute.registerUser(user7);
        ResponseMessage registerMessage8 = createUserRoute.registerUser(user8);
        ResponseMessage registerMessage9 = createUserRoute.registerUser(user9);
        ResponseMessage registerMessage10 = createUserRoute.registerUser(null);
        ResponseMessage registerMessage11 = createUserRoute.registerUser(user11);
        ResponseMessage registerMessage12 = createUserRoute.registerUser(user12);


        // Then
        assertTrue(registerMessage.isStatus());

        assertFalse(registerMessage1.isStatus());
        assertFalse(registerMessage2.isStatus());
        assertFalse(registerMessage3.isStatus());
        assertFalse(registerMessage4.isStatus());
        assertFalse(registerMessage5.isStatus());
        assertFalse(registerMessage6.isStatus());
        assertFalse(registerMessage7.isStatus());
        assertFalse(registerMessage8.isStatus());
        assertFalse(registerMessage9.isStatus());
        assertFalse(registerMessage10.isStatus());
        assertFalse(registerMessage11.isStatus());
        assertFalse(registerMessage12.isStatus());

    }


}
