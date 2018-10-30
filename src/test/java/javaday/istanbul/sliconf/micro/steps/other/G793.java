package javaday.istanbul.sliconf.micro.steps.other;

import cucumber.api.java.Before;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.builder.UserBuilder;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.LoginControllerMessageProvider;
import javaday.istanbul.sliconf.micro.service.UserPassService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

@Ignore
public class G793 extends SpringBootTestConfig {

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Autowired
    private LoginControllerMessageProvider loginControllerMessageProvider;

    private User user;

    @Before
    public void init() {
        // userRepositoryService = mock(UserRepositoryService.class);
    }

    @Eğerki("^eposta adresi sistemde daha önceden kayıtlı ise$")
    public void eposta_adresi_sistemde_daha_önceden_kayıtlı_ise() throws Throwable {
        user = new UserBuilder()
                .setName("Osman Uykulu")
                .setEmail("osman14@osman.com")
                .setPassword("1234!")
                .build();

        UserPassService userPassService = new UserPassService();
        user = userPassService.createNewUserWithHashedPassword(user);

        userRepositoryService.save(user);

        assertNotNull(user);
    }

    @Ozaman("^sistem potansiyel etkinlik sahibini çelişkiyi haber verir\\.$")
    public void sistem_potansiyel_etkinlik_sahibini_çelişkiyi_haber_verir() throws Throwable {
        ResponseMessage responseMessage = null;

        //when(userRepositoryService.controlIfEmailIsExists(user.getEmail())).thenReturn(true);

        if (userRepositoryService.controlIfEmailIsExists(user.getEmail())) {
            responseMessage = new ResponseMessage(false,
                    loginControllerMessageProvider.getMessage("emailAlreadyUsed"), new Object());
        }

        assertNotNull(responseMessage);
    }
}
