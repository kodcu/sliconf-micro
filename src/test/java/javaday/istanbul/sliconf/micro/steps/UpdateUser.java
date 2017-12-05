package javaday.istanbul.sliconf.micro.steps;

import com.couchbase.client.java.document.json.JsonObject;
import cucumber.api.java.Before;
import cucumber.api.java.tr.Diyelimki;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.controller.login.LoginUserRoute;
import javaday.istanbul.sliconf.micro.controller.login.UpdateUserRoute;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
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
public class UpdateUser {

    @Autowired
    UserRepositoryService userRepositoryService;

    @Autowired
    UpdateUserRoute updateUserRoute;

    @Autowired
    LoginUserRoute loginUserRoute;

    private User user;

    private String id;

    @Before
    public void init() {
        // Given
        user.setEmail("osmanUpdate@baykal.com");
        user.setUsername("osmanbaykalUpdate");
        user.setPassword("123123123");

        ResponseMessage responseMessageUser = userRepositoryService.saveUser(user);

        id = ((User) responseMessageUser.getReturnObject()).getId();
    }

    @Diyelimki("^Kullanici sifresini degistirmek istedi$")
    public void kullaniciSifresiniDegistirmekIstedi() throws Throwable {

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

        User loginUser = new User();
        loginUser.setUsername("osmanbaykalUpdate");
        loginUser.setEmail("osmanUpdate@baykal.com");
        loginUser.setPassword("tlp123123");

        ResponseMessage responseMessageLogin = loginUserRoute.loginUser(loginUser);

        // Then

        assertTrue(responseMessageUpdate.isStatus());

        assertFalse(responseMessageUpdateNullId.isStatus());

        assertTrue(responseMessageLogin.isStatus());
    }

}
