package javaday.istanbul.sliconf.micro.steps;

import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.builder.UserBuilder;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.UserPassService;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@ContextConfiguration(classes = {CucumberConfiguration.class})

public class G685 {

    private User user;

    private User dbUser;

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Autowired
    private EventRepositoryService eventRepositoryService;


    private UserPassService userPassService = new UserPassService();

    @Diyelimki("^etkinlik sahibi daha önceden JugEvents sistemine başarılı bir şekilde kayıt olmuş$")
    public void etkinlik_sahibi_daha_önceden_JugEvents_sistemine_başarılı_bir_şekilde_kayıt_olmuş() throws Throwable {
        user = new UserBuilder()
                .setName("Osman Uykulu")
                .setEmail("osman@osman.com")
                .setPassword("1234!")
                .build();

        assertNotNull(user);
    }

    @Eğerki("^etkinlik sahibi,  kulanıcı adı ve şifresini doğru bir şekilde girerse$")
    public void etkinlik_sahibi_kulanıcı_adı_ve_şifresini_doğru_bir_şekilde_girerse() throws Throwable {
        dbUser = userRepositoryService.findFirstByEmailEquals(this.user.getEmail());

        assertTrue(userPassService.checkIfUserAuthenticated(dbUser, user));
    }

    @Ozaman("^sistem etkinlik sahibininin açmış olduğu aktif ve/veya pasif etkinlikleri getirmeli\\.$")
    public void sistem_etkinlik_sahibininin_açmış_olduğu_aktif_ve_veya_pasif_etkinlikleri_getirmeli() throws Throwable {
        Map<String, Event> events = eventRepositoryService.findByExecutiveUser(dbUser.getId());

        assertNotNull(events);
    }

}
