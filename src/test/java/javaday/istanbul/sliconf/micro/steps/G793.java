package javaday.istanbul.sliconf.micro.steps;

import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.builder.UserBuilder;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.LoginControllerMessageProvider;
import javaday.istanbul.sliconf.micro.service.UserPassService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertNotNull;


@ContextConfiguration(classes = {CucumberConfiguration.class})
public class G793 {

    @Autowired
    UserRepositoryService userRepositoryService;

    @Autowired
    private LoginControllerMessageProvider loginControllerMessageProvider;

    private User user;

    @Eğerki("^eposta adresi sistemde daha önceden kayıtlı ise$")
    public void eposta_adresi_sistemde_daha_önceden_kayıtlı_ise() throws Throwable {
        user = new UserBuilder()
                .setName("Osman Uykulu")
                .setEmail("osman@osman.com")
                .setPassword("1234!")
                .build();

        UserPassService userPassService = new UserPassService();
        user = userPassService.createNewUserWithHashedPassword(user);

        assertNotNull(user);
    }

    @Ozaman("^sistem potansiyel etkinlik sahibini çelişkiyi haber verir\\.$")
    public void sistem_potansiyel_etkinlik_sahibini_çelişkiyi_haber_verir() throws Throwable {
        ResponseMessage responseMessage = null;

        if (userRepositoryService.controlIfEmailIsExists(user.getEmail())) {
            responseMessage = new ResponseMessage(false,
                    loginControllerMessageProvider.getMessage("emailAlreadyUsed"), new Object());
        }

        assertNotNull(responseMessage);
    }
}
