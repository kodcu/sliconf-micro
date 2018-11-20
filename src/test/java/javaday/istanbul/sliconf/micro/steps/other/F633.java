package javaday.istanbul.sliconf.micro.steps.other;


import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.event.EventBuilder;
import javaday.istanbul.sliconf.micro.event.EventSpecs;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.event.service.EventRepositoryService;
import javaday.istanbul.sliconf.micro.user.UserBuilder;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.user.service.UserPassService;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import javaday.istanbul.sliconf.micro.util.Constants;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@Ignore
public class F633 extends SpringBootTestConfig {

    @Autowired
    private EventRepositoryService eventRepositoryService;

    @Autowired
    private UserRepositoryService userRepositoryService;

    private Event event;
    private User user;


    @Diyelimki("^potansiyel etkinlik sahibi JugEvents sisteminde yeni bir etkinlik açmak istedi$")
    public void potansiyelEtkinlikSahibiJugEventsSistemindeYeniBirEtkinlikAçmakIstedi() throws Throwable {
        event = new EventBuilder().setName("javaday 2018").
                setDate(LocalDateTime.of(4018, 5, 27, 10, 0)).build();

        assertNotNull(event);
    }

    @Eğerki("^potansyel etkinlik sahibi,  ad, soyad, eposta ve şifre bilgisini eksiksiz vermişse$")
    public void potansyelEtkinlikSahibiAdSoyadEpostaVeŞifreBilgisiniEksiksizVermişse() throws Throwable {
        user = new UserBuilder()
                .setName("Osman Uykulu")
                .setEmail("osman12@osman.com")
                .setPassword("1234!")
                .build();

        UserPassService userPassService = new UserPassService();
        user = userPassService.createNewUserWithHashedPassword(user);

        assertNotNull(user);
    }

    @Eğerki("^eposta adresi sistemde daha önceden kayıtlı değilse$")
    public void epostaAdresiSistemdeDahaÖncedenKayıtlıDeğilse() throws Throwable {
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

        String kanbanNumber = EventSpecs.generateKanbanNumber(event, eventRepositoryService);

        event.setExecutiveUser(user.getId());

        assertEquals(Constants.EVENT_KEY_LENGTH, kanbanNumber.length());
    }

    @Ozaman("^sistem etkinlik sahibini kayıt eder ve etkinlik oluşturulmuş olur$")
    public void sistemEtkinlikSahibiniKayıtEderVeEtkinlikOluşturulmuşOlur() throws Throwable {
        assertTrue(userRepositoryService.save(user).isStatus());
        assertTrue(eventRepositoryService.save(event).isStatus());
    }
}
