package com.sliconf;


import cucumber.api.PendingException;
import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class F633 {


    @Autowired
    EventRepositoryService eventRepositoryService;

    @Autowired
    UserRepositoryService userRepositoryService;

    Event event = null;
    User user = null ;

    @Diyelimki("^potansiyel etkinlik sahibi JugEvents sisteminde yeni bir etkinlik açmak istedi$")
    public void potansiyelEtkinlikSahibiJugEventsSistemindeYeniBirEtkinlikAçmakIstedi() throws Throwable {
        event = new Event();
        assertNotNull(event);
    }

    @Eğerki("^potansyel etkinlik sahibi,  ad, soyad, eposta ve şifre bilgisini eksiksiz vermişse$")
    public void potansyelEtkinlikSahibiAdSoyadEpostaVeŞifreBilgisiniEksiksizVermişse() throws Throwable {
        user = new User();
        user.setName("Osman");
        user.setName("Uykulu");
        user.setEmail("osman@osman.com");
        user.setPassword("1234!");

        assertNotNull(user);
    }

    @Eğerki("^eposta adresi sistemde daha önceden kayıtlı değilse$")
    public void epostaAdresiSistemdeDahaÖncedenKayıtlıDeğilse() throws Throwable {
       assertFalse(EmailUtil.controlIfEmailIsExists(user.getEmail()));
    }

    @Eğerki("^etkinliği adı asgari düzeyde yeterliyse - min (\\d+) harf ise$")
    public void etkinliğiAdıAsgariDüzeydeYeterliyseMinHarfIse(int arg1) throws Throwable {

        assertTrue(EventUtil.checkEventName(event, arg1));
    }

    @Eğerki("^etkinliğin tarihi bugün veya daha ileri bir tarih olarak belirtlişse$")
    public void etkinliğinTarihiBugünVeyaDahaIleriBirTarihOlarakBelirtlişse() throws Throwable {
        assertTrue(EventUtil.checkEventDate(event));
    }

    @Ozaman("^sistem etkinlik sahibini kayıt eder ve etkinlik oluşturulmuş olur$")
    public void sistemEtkinlikSahibiniKayıtEderVeEtkinlikOluşturulmuşOlur() throws Throwable {
        userRepositoryService.save(user);
        eventRepositoryService.save(event);

        assertNotNull(user.getId());
        assertNotNull(event.getId());
    }

    @Ozaman("^sistem etkinliğe özel ve eşşiz bir etkinlik kodu üretir\\.$")
    public void sistemEtkinliğeÖzelVeEşşizBirEtkinlikKoduÜretir() throws Throwable {

        String kanbanNumber = EventUtil.generateKanbanNumber(event);
        assertEquals(kanbanNumber.length(), 4);
    }
}
