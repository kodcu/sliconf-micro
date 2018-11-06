package javaday.istanbul.sliconf.micro.steps.other;


import com.couchbase.client.java.document.json.JsonObject;
import cucumber.api.java.Before;
import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.builder.UserBuilder;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.UserPassService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import org.junit.Ignore;

import static javaday.istanbul.sliconf.micro.specs.UserSpecs.checkUserParams;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Ignore
public class A265 extends SpringBootTestConfig {
    private User user;
    private User dbUser;

    private UserRepositoryService userRepositoryService;
    private UserPassService userPassService = new UserPassService();

    private JsonObject request = JsonObject.create();

    @Before
    public void init() {
        request.put("email", "osman@osman.com");
        userRepositoryService = mock(UserRepositoryService.class);
        dbUser = userPassService.createNewUserWithHashedPassword(new UserBuilder().setName("Osman Uykulu").setEmail("osman@osman.com").setPassword("1234!").build());
    }

    @Diyelimki("^etkinlik sahibi ismini değiştimek istiyor$")
    public void etkinlik_sahibi_ismini_değiştimek_istiyor() throws Throwable {
        user = new UserBuilder()
                .setName("Osman Uykulu")
                .setEmail("osman@osman.com")
                .setPassword("1234!")
                .build();
        if (userRepositoryService.controlIfEmailIsExists(user.getEmail())) {
            assertTrue(userPassService.checkIfUserAuthenticated(dbUser, user));
        }
        request.put("name", "Anıl Müslüm");

    }

    @Eğerki("^yeni ismi asgari (\\d+) harf girilmişse$")
    public void yeni_ismi_asgari_harf_girilmişse(int arg1) throws Throwable {
        assertTrue(checkUserParams(request.getString("name"), arg1));
    }

    @Ozaman("^sistem isim değişikliğe izin verir$")
    public void sistem_isim_değişikliğe_izin_verir() throws Throwable {
        user.setUsername(request.getString("name"));
        ResponseMessage userMsg = new ResponseMessage(true, "User saved successfully!", user);
        when(userRepositoryService.save(user)).thenReturn(userMsg);
        ResponseMessage responseMessage = userRepositoryService.save(user);
        assertEquals(user.getUsername(), request.getString("name"));
    }

    @Diyelimki("^etkinlik sahibi şifresini değiştimek istiyor$")
    public void etkinlik_sahibi_şifresini_değiştimek_istiyor() throws Throwable {
        request.put("pass", "kodcu");
    }

    @Eğerki("^etkinlik sahibi şifresini asgari (\\d+) harf girmişse$")
    public void etkinlik_sahibi_şifresini_asgari_harf_girmişse(int arg1) throws Throwable {
        assertTrue(checkUserParams(request.getString("pass"), arg1));
    }

    @Ozaman("^sistem şifre değişikliğe izin verir$")
    public void sistem_şifre_değişikliğe_izin_verir() throws Throwable {
        user.setPassword(request.getString("pass"));
        ResponseMessage userMsg = new ResponseMessage(true, "User saved successfully!", user);
        when(userRepositoryService.save(user)).thenReturn(userMsg);
        ResponseMessage responseMessage = userRepositoryService.save(user);
        assertEquals(user.getPassword(), request.getString("pass"));

    }

    @Diyelimki("^etkinlik sahibi eposta epostasını değiştirmek istiyor$")
    public void etkinlik_sahibi_eposta_epostasını_değiştirmek_istiyor() throws Throwable {
        request.put("email", "kodcu@kodcu.com");
    }

    @Ozaman("^sistem eposta değişikliğe izin verir vermemeli$")
    public void sistem_eposta_değişikliğe_izin_verir_vermemeli() throws Throwable {
        user.setEmail(request.getString("email"));
        ResponseMessage userMsg = new ResponseMessage(true, "You cannot change email!", user);
        when(userRepositoryService.save(user)).thenReturn(userMsg);
        ResponseMessage responseMessage = userRepositoryService.save(user);
        assertEquals("You cannot change email!", responseMessage.getMessage());
    }

}
