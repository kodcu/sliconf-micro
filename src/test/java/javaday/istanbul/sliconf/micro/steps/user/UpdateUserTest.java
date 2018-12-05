package javaday.istanbul.sliconf.micro.steps.user;

import com.couchbase.client.java.document.json.JsonObject;
import cucumber.api.java.tr.Diyelimki;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.user.controller.LoginUserRoute;
import javaday.istanbul.sliconf.micro.user.controller.UpdateUserRoute;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Ignore
public class UpdateUserTest extends SpringBootTestConfig { // NOSONAR

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

        // Given
        User user13 = new User();
        user13.setUsername("osmanbaykalUpdate13");
        user13.setEmail("osmanUpdate13@baykal.com");
        user13.setPassword("tlp123123");

        ResponseMessage responseMessageUser13 = userRepositoryService.saveUser(user13);

        User loginUser1 = new User();
        loginUser1.setUsername("osmanbaykalUpdate13");
        loginUser1.setEmail("osmanUpdate13@baykal.com");
        loginUser1.setPassword("tlp123123");

        ResponseMessage responseMessageLogin1 = loginUserRoute.loginUser(loginUser1);

        loginUser1.setUsername("osmanUpdate13@baykal.com");

        ResponseMessage responseMessageLogin2 = loginUserRoute.loginUser(loginUser1);

        loginUser1.setUsername("osmanbaykalUpdate13");
        loginUser1.setPassword("123ddd");
        ResponseMessage responseMessageLogin3 = loginUserRoute.loginUser(loginUser1);

        loginUser1.setPassword("");
        ResponseMessage responseMessageLogin4 = loginUserRoute.loginUser(loginUser1);

        loginUser1.setPassword(null);
        ResponseMessage responseMessageLogin5 = loginUserRoute.loginUser(loginUser1);

        loginUser1.setPassword("tlp123123");
        loginUser1.setUsername("username133123");
        ResponseMessage responseMessageLogin6 = loginUserRoute.loginUser(loginUser1);


        // Then

        assertTrue(responseMessageUpdate.isStatus());
        assertTrue(responseMessageUpdateNullPass.isStatus());

        assertFalse(responseMessageUpdateNullId.isStatus());
        assertFalse(responseMessageUpdateDiffId.isStatus());
        assertFalse(responseMessageUpdateAlreadyUsedName.isStatus());

        assertTrue(responseMessageLogin.isStatus());

        assertTrue(responseMessageLogin1.isStatus());
        assertTrue(responseMessageLogin2.isStatus());

        assertFalse(responseMessageLogin3.isStatus());
        assertFalse(responseMessageLogin4.isStatus());
        assertFalse(responseMessageLogin5.isStatus());
        assertFalse(responseMessageLogin6.isStatus());
    }

}
