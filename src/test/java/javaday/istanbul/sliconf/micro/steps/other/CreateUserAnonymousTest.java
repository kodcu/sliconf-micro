package javaday.istanbul.sliconf.micro.steps.other;

import cucumber.api.java.tr.Diyelimki;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.user.controller.CreateUserAnonymousRoute;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Ignore
public class CreateUserAnonymousTest extends SpringBootTestConfig { // NOSONAR

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
