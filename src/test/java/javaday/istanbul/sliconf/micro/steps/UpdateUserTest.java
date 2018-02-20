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
public class UpdateUserTest {// NOSONAR

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

        // Given
        User user1 = new User();
        user1.setEmail("osmanUpdate12@baykal.com");
        user1.setUsername("osmanbaykalUpdate12");
        user1.setPassword("123123123");

        ResponseMessage responseMessageUser1 = userRepositoryService.saveUser(user1);

        String id2 = ((User) responseMessageUser1.getReturnObject()).getId();

        // When

        String body = "{\"id\" : \"" + id + "\"," +
                "\"oldpassword\": \"123123123\"," +
                "\"password\": \"tlp123123\" }";

        String bodyNullId = "{" +
                "\"oldpassword\": \"123123123\"," +
                "\"password\": \"tlp123123\" }";

        String bodyDiffrentId = "{" +
                "\"id\": \"diffrentId123\"," +
                "\"oldpassword\": \"123123123\"," +
                "\"password\": \"tlp123123\" }";

        String bodyNullPassword = "{\"id\" : \"" + id + "\"," +
                "\"username\": \"diffUsername\" }";


        String bodyAlreadyUsedName = "{\"id\" : \"" + id + "\"," +
                "\"username\": \"osmanbaykalUpdate12\" }";

        JsonObject updateParams = JsonObject.fromJson(body);
        JsonObject updateParamsNullId = JsonObject.fromJson(bodyNullId);
        JsonObject updateParamsDiffrentId = JsonObject.fromJson(bodyDiffrentId);
        JsonObject updateParamsNullPass = JsonObject.fromJson(bodyNullPassword);
        JsonObject updateParamsAlreadyUsedName = JsonObject.fromJson(bodyAlreadyUsedName);

        ResponseMessage responseMessageUpdate = updateUserRoute.updateUser(updateParams);
        ResponseMessage responseMessageUpdateNullId = updateUserRoute.updateUser(updateParamsNullId);
        ResponseMessage responseMessageUpdateDiffId = updateUserRoute.updateUser(updateParamsDiffrentId);



        User loginUser = new User();
        loginUser.setUsername("osmanbaykalUpdate");
        loginUser.setEmail("osmanUpdate@baykal.com");
        loginUser.setPassword("tlp123123");

        ResponseMessage responseMessageLogin = loginUserRoute.loginUser(loginUser);


        ResponseMessage responseMessageUpdateNullPass = updateUserRoute.updateUser(updateParamsNullPass);
        ResponseMessage responseMessageUpdateAlreadyUsedName = updateUserRoute.updateUser(updateParamsAlreadyUsedName);


        // Then

        assertTrue(responseMessageUpdate.isStatus());
        assertTrue(responseMessageUpdateNullPass.isStatus());

        assertFalse(responseMessageUpdateNullId.isStatus());
        assertFalse(responseMessageUpdateDiffId.isStatus());
        assertFalse(responseMessageUpdateAlreadyUsedName.isStatus());

        assertTrue(responseMessageLogin.isStatus());
    }

}
