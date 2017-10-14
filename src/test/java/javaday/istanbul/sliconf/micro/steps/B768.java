package javaday.istanbul.sliconf.micro.steps;

import cucumber.api.java.Before;
import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.builder.UserBuilder;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.service.UserPassService;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {CucumberConfiguration.class})
@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class B768 {

    private User user;
    private User dbUser;

    private UserRepositoryService userRepositoryService;
    private EventRepositoryService eventRepositoryService;
    private UserPassService userPassService = new UserPassService();


    @Before
    public void init() {
        userRepositoryService = mock(UserRepositoryService.class);
        eventRepositoryService = mock(EventRepositoryService.class);
        dbUser = userPassService.createNewUserWithHashedPassword(new UserBuilder().setEmail("osman@osman.com").setPassword("1234!").build());
    }

    @Diyelimki("^etkinlik sahibi sisteme başarılı bir şekilde giriş yaptı, herşey yolunda$")
    public void etkinlik_sahibi_sisteme_başarılı_bir_şekilde_giriş_yaptı_herşey_yolunda() throws Throwable {
        user = new UserBuilder()
                .setEmail("osman@osman.com")
                .setPassword("1234!")
                .build();
        if (userRepositoryService.controlIfEmailIsExists(user.getEmail())) {
            assertTrue(userPassService.checkIfUserAuthenticated(dbUser, user));
        }
    }

    @Eğerki("^etkinlik sahibi etkinliklerini listelemek isterse$")
    public void etkinlik_sahibi_etkinliklerini_listelemek_isterse() throws Throwable {
        when(eventRepositoryService.findByExecutiveUser(dbUser.getId())).thenReturn(new HashMap<>());
    }

    @Ozaman("^sistem etkinlik sahibinin geçmiş ve gelecek tüm etkinliklerini listeler$")
    public void sistem_etkinlik_sahibinin_geçmiş_ve_gelecek_tüm_etkinliklerini_listeler() throws Throwable {

        Map<String, List<Event>> events = eventRepositoryService.findByExecutiveUser(dbUser.getId());
        assertNotNull(events);
    }
}
