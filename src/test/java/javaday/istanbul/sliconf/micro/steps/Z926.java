package javaday.istanbul.sliconf.micro.steps;

import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.builder.EventBuilder;
import javaday.istanbul.sliconf.micro.builder.UserBuilder;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.UserPassService;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(classes = {CucumberConfiguration.class})
public class Z926 {

    @Autowired
    EventRepositoryService eventRepositoryService;

    @Autowired
    UserRepositoryService userRepositoryService;

    private Event event;
    private User user;

    private List<User> users;

    @Diyelimki("^etkinlik sahibi daha önceden JugEvents sistemine kayıtlıdır$")
    public void etkinlik_sahibi_daha_önceden_JugEvents_sistemine_kayıtlıdır() throws Throwable {
        user = new UserBuilder()
                .setName("Osman Uykulu")
                .setEmail("osman@osman.com")
                .setPassword("1234!")
                .build();

        UserPassService userPassService = new UserPassService();
        user = userPassService.createNewUserWithHashedPassword(user);

        assertNotNull(user);
    }

    @Eğerki("^etkinlik sahibi yeni bir etkinliği etkinlik kurallarına uygun bir şekilde girerse$")
    public void etkinlik_sahibi_yeni_bir_etkinliği_etkinlik_kurallarına_uygun_bir_şekilde_girerse() throws Throwable {
        users = userRepositoryService.findByEmail(user.getEmail());


        assertNotNull(users);

        assertNotNull(users.get(0));

        user.setId(users.get(0).getId());


        event = new EventBuilder().setName("javaday 2018 - 1").
                setDate(LocalDateTime.of(2018, 5, 27, 10, 0)).
                setExecutiveUser(user.getId()).build();

        assertNotNull(event);
    }

    @Ozaman("^sistem etkinlik sahibinin yeni etkinliğini başarılı bir şekilde oluşturur\\.$")
    public void sistem_etkinlik_sahibinin_yeni_etkinliğini_başarılı_bir_şekilde_oluşturur() throws Throwable {
        ResponseMessage eventMessage = eventRepositoryService.save(event);

        assertTrue(eventMessage.isStatus());
    }
}
