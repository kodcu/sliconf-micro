package javaday.istanbul.sliconf.micro.steps;


import cucumber.api.java.Before;
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
import javaday.istanbul.sliconf.micro.specs.EventSpecs;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {CucumberConfiguration.class})
public class F633 {

    private EventRepositoryService eventRepositoryService;
    private UserRepositoryService userRepositoryService;

    private Event event;
    private User user;

    @Before
    public void init() {
        userRepositoryService = mock(UserRepositoryService.class);
        eventRepositoryService = mock(EventRepositoryService.class);
    }

    @Diyelimki("^potansiyel etkinlik sahibi JugEvents sisteminde yeni bir etkinlik açmak istedi$")
    public void potansiyelEtkinlikSahibiJugEventsSistemindeYeniBirEtkinlikAçmakIstedi() throws Throwable {
        event = new EventBuilder().setName("javaday 2018").
                setDate(LocalDateTime.of(2018, 5, 27, 10, 0)).build();

        assertNotNull(event);
    }

    @Eğerki("^potansyel etkinlik sahibi,  ad, soyad, eposta ve şifre bilgisini eksiksiz vermişse$")
    public void potansyelEtkinlikSahibiAdSoyadEpostaVeŞifreBilgisiniEksiksizVermişse() throws Throwable {
        user = new UserBuilder()
                .setName("Osman Uykulu")
                .setEmail("osman@osman.com")
                .setPassword("1234!")
                .build();

        UserPassService userPassService = new UserPassService();
        user = userPassService.createNewUserWithHashedPassword(user);

        assertNotNull(user);
    }

    @Eğerki("^eposta adresi sistemde daha önceden kayıtlı değilse$")
    public void epostaAdresiSistemdeDahaÖncedenKayıtlıDeğilse() throws Throwable {
        when(userRepositoryService.controlIfEmailIsExists(user.getEmail()))
                .thenReturn(false);

        assertFalse(userRepositoryService.controlIfEmailIsExists(user.getEmail()));
    }

    @Eğerki("^etkinliği adı asgari düzeyde yeterliyse - min (\\d+) harf ise$")
    public void etkinliğiAdıAsgariDüzeydeYeterliyseMinHarfIse(int arg1) throws Throwable {
        assertTrue(EventSpecs.checkEventName(event, arg1));
    }

    @Eğerki("^etkinliğin tarihi bugün veya daha ileri bir tarih olarak belirtlişse$")
    public void etkinliğinTarihiBugünVeyaDahaIleriBirTarihOlarakBelirtlişse() throws Throwable {
        assertTrue(EventSpecs.checkIfEventDateAfterOrInNow(event));
    }

    @Ozaman("^sistem etkinliğe özel ve eşşiz bir etkinlik kodu üretir\\.$")
    public void sistemEtkinliğeÖzelVeEşşizBirEtkinlikKoduÜretir() throws Throwable {

        String kanbanNumber = EventSpecs.generateKanbanNumber(event);

        event.setExecutiveUser(user.getId());

        assertEquals(kanbanNumber.length(), 4);
    }

    @Ozaman("^sistem etkinlik sahibini kayıt eder ve etkinlik oluşturulmuş olur$")
    public void sistemEtkinlikSahibiniKayıtEderVeEtkinlikOluşturulmuşOlur() throws Throwable {
        ResponseMessage userTemp = new ResponseMessage(true, "User saved successfully!", user);
        ResponseMessage eventTemp = new ResponseMessage(true, "Event saved successfully!", event);

        when(userRepositoryService.save(user))
                .thenReturn(userTemp);

        when(eventRepositoryService.save(event))
                .thenReturn(eventTemp);


        assertTrue(userRepositoryService.save(user).isStatus());
        assertTrue(eventRepositoryService.save(event).isStatus());
    }
}
