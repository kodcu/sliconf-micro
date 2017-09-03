package javaday.istanbul.sliconf.micro.steps;


import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.builder.UserBuilder;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.util.EventUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@ContextConfiguration(classes = {CucumberConfiguration.class})
public class F633 {


    @Autowired
    EventRepositoryService eventRepositoryService;

    @Autowired
    UserRepositoryService userRepositoryService;

    private Event event;
    private User user;

    @Diyelimki("^potansiyel etkinlik sahibi JugEvents sisteminde yeni bir etkinlik açmak istedi$")
    public void potansiyelEtkinlikSahibiJugEventsSistemindeYeniBirEtkinlikAçmakIstedi() throws Throwable {
        event = new Event();

        event.setName("javaday 2018");
        event.setDate(LocalDateTime.of(2018, 5, 27, 10, 0));

        assertNotNull(event);
    }

    @Eğerki("^potansyel etkinlik sahibi,  ad, soyad, eposta ve şifre bilgisini eksiksiz vermişse$")
    public void potansyelEtkinlikSahibiAdSoyadEpostaVeŞifreBilgisiniEksiksizVermişse() throws Throwable {
        user = new UserBuilder()
                .setName("Osman Uykulu")
                .setEmail("osman@osman.com")
                .setPassword("1234!")
                .build();

        assertNotNull(user);
    }

    @Eğerki("^eposta adresi sistemde daha önceden kayıtlı değilse$")
    public void epostaAdresiSistemdeDahaÖncedenKayıtlıDeğilse() throws Throwable {
        assertFalse(userRepositoryService.controlIfEmailIsExists(user.getEmail()));
    }

    @Eğerki("^etkinliği adı asgari düzeyde yeterliyse - min (\\d+) harf ise$")
    public void etkinliğiAdıAsgariDüzeydeYeterliyseMinHarfIse(int arg1) throws Throwable {
        assertTrue(EventUtil.checkEventName(event, arg1));
    }

    @Eğerki("^etkinliğin tarihi bugün veya daha ileri bir tarih olarak belirtlişse$")
    public void etkinliğinTarihiBugünVeyaDahaIleriBirTarihOlarakBelirtlişse() throws Throwable {
        assertTrue(EventUtil.checkIfEventDateAfterOrInNow(event));
    }

    @Ozaman("^sistem etkinlik sahibini kayıt eder ve etkinlik oluşturulmuş olur$")
    public void sistemEtkinlikSahibiniKayıtEderVeEtkinlikOluşturulmuşOlur() throws Throwable {
        ResponseMessage userMessage = userRepositoryService.save(user);
        ResponseMessage eventMessage = eventRepositoryService.save(event);

        assertTrue(userMessage.isStatus());
        assertTrue(eventMessage.isStatus());
    }

    @Ozaman("^sistem etkinliğe özel ve eşşiz bir etkinlik kodu üretir\\.$")
    public void sistemEtkinliğeÖzelVeEşşizBirEtkinlikKoduÜretir() throws Throwable {

        String kanbanNumber = EventUtil.generateKanbanNumber(event);
        assertEquals(kanbanNumber.length(), 4);
    }
}
