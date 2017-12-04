package javaday.istanbul.sliconf.micro.steps;

import com.couchbase.client.java.document.json.JsonObject;
import cucumber.api.java.Before;
import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.builder.UserBuilder;
import javaday.istanbul.sliconf.micro.controller.login.LoginUserRoute;
import javaday.istanbul.sliconf.micro.controller.login.UpdateUserRoute;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.model.token.Token;
import javaday.istanbul.sliconf.micro.service.PasswordResetService;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.token.TokenRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;


@ContextConfiguration(classes = {CucumberConfiguration.class})
@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class UpdateUserTest {

    @Autowired
    UserRepositoryService userRepositoryService;

    @Autowired
    UpdateUserRoute updateUserRoute;

    @Autowired
    LoginUserRoute loginUserRoute;

    @Diyelimki("^Kullanici sifresini degistirmek istedi$")
    public void kullaniciSifresiniDegistirmekIstedi() throws Throwable {
        // Given
        User user = new User();
        user.setEmail("osmanUpdate@baykal.com");
        user.setUsername("osmanbaykalUpdate");
        user.setPassword("123123123");

        ResponseMessage responseMessageUser = userRepositoryService.saveUser(user);

        String id = ((User) responseMessageUser.getReturnObject()).getId();

        User loginUser = new User();
        loginUser.setUsername("osmanbaykalUpdate");
        loginUser.setEmail("osmanUpdate@baykal.com");
        loginUser.setPassword("tlp123123");

        // When

        String body = "{\"id\" : \"" + id + "\"," +
                "\"oldpassword\": \"123123123\"," +
                "\"password\": \"tlp123123\" }";

        String bodyNullId = "{" +
                "\"oldpassword\": \"123123123\"," +
                "\"password\": \"tlp123123\" }";

        JsonObject updateParams = JsonObject.fromJson(body);
        JsonObject updateParamsNullId = JsonObject.fromJson(bodyNullId);

        ResponseMessage responseMessageUpdate = updateUserRoute.updateUser(updateParams);
        ResponseMessage responseMessageUpdateNullId = updateUserRoute.updateUser(updateParamsNullId);

        ResponseMessage responseMessageLogin = loginUserRoute.loginUser(loginUser);

        // Then

        assertTrue(responseMessageUpdate.isStatus());

        assertFalse(responseMessageUpdateNullId.isStatus());

        assertTrue(responseMessageLogin.isStatus());
    }

}
