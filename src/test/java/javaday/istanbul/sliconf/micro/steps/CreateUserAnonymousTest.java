package javaday.istanbul.sliconf.micro.steps;

import cucumber.api.java.tr.Diyelimki;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.controller.login.CreateUserAnonymousRoute;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
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
public class CreateUserAnonymousTest {// NOSONAR

    @Autowired
    CreateUserAnonymousRoute createUserAnonymousRoute;

    @Diyelimki("^Anonim Kullanici Olusturuluyor$")
    public void anonimKullaniciOlusturuluyor() throws Throwable {

        String deviceId1 = "deviceId1";
        String deviceId2 = "deviceId1";

        String deviceId3 = "";
        String deviceId4 = null;


        // When
        ResponseMessage createUserAnonymousMessage1 = createUserAnonymousRoute.createAnonymousUser(deviceId1);
        ResponseMessage createUserAnonymousMessage2 = createUserAnonymousRoute.createAnonymousUser(deviceId2);
        ResponseMessage createUserAnonymousMessage3 = createUserAnonymousRoute.createAnonymousUser(deviceId3);
        ResponseMessage createUserAnonymousMessage4 = createUserAnonymousRoute.createAnonymousUser(deviceId4);


        // Then
        assertTrue(createUserAnonymousMessage1.isStatus());

        assertFalse(createUserAnonymousMessage2.isStatus());
        assertFalse(createUserAnonymousMessage3.isStatus());
        assertFalse(createUserAnonymousMessage4.isStatus());
    }


}
