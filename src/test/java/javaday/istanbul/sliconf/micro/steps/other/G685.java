package javaday.istanbul.sliconf.micro.steps.other;

import cucumber.api.java.Before;
import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.event.service.EventRepositoryService;
import javaday.istanbul.sliconf.micro.user.util.UserBuilder;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.user.service.UserPassService;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Ignore
public class G685 extends SpringBootTestConfig {

    private User user;
    private User dbUser;

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Autowired
    private EventRepositoryService eventRepositoryService;

    private UserPassService userPassService = new UserPassService();

    @Before
    public void init() {
        // userRepositoryService = mock(UserRepositoryService.class);
        // eventRepositoryService = mock(EventRepositoryService.class);
    }

    @Diyelimki("^etkinlik sahibi daha önceden JugEvents sistemine başarılı bir şekilde kayıt olmuş$")
    public void etkinlik_sahibi_daha_önceden_JugEvents_sistemine_başarılı_bir_şekilde_kayıt_olmuş() throws Throwable {
        user = new UserBuilder()
                .setName("Osman Uykulu")
                .setEmail("osman13@osman.com")
                .setPassword("1234!")
                .build();

        assertNotNull(user);
    }

    @Eğerki("^etkinlik sahibi,  kulanıcı adı ve şifresini doğru bir şekilde girerse$")
    public void etkinlik_sahibi_kulanıcı_adı_ve_şifresini_doğru_bir_şekilde_girerse() throws Throwable {

        User tempUser = new UserBuilder()
                .setName("Osman Uykulu")
                .setEmail("osman13@osman.com")
                .setPassword("1234!")
                .build();

        tempUser = userPassService.createNewUserWithHashedPassword(tempUser);

        userRepositoryService.save(tempUser);

        dbUser = userRepositoryService.findByEmail(this.user.getEmail());

        assertTrue(userPassService.checkIfUserAuthenticated(tempUser, user));
    }

    @Ozaman("^sistem etkinlik sahibininin açmış olduğu aktif ve/veya pasif etkinlikleri getirmeli\\.$")
    public void sistem_etkinlik_sahibininin_açmış_olduğu_aktif_ve_veya_pasif_etkinlikleri_getirmeli() throws Throwable {

        // when(eventRepositoryService.findByExecutiveUser(dbUser.getId())).thenReturn(new HashMap<>());

        Map<String, List<Event>> events = eventRepositoryService.findByExecutiveUser(dbUser.getId());

        assertNotNull(events);
    }

}
