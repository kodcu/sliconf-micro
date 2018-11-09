package javaday.istanbul.sliconf.micro.steps.admin;

import cucumber.api.PendingException;
import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import cucumber.api.java.tr.Ve;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.admin.AdminService;
import javaday.istanbul.sliconf.micro.agenda.AgendaGenerator;
import javaday.istanbul.sliconf.micro.event.EventGenerator;
import javaday.istanbul.sliconf.micro.event.EventSpecs;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.event.model.LifeCycleState;
import javaday.istanbul.sliconf.micro.event.repository.EventRepository;
import javaday.istanbul.sliconf.micro.event.service.EventRepositoryService;
import javaday.istanbul.sliconf.micro.floor.FloorGenerator;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.room.RoomGenerator;
import javaday.istanbul.sliconf.micro.security.TokenAuthenticationService;
import javaday.istanbul.sliconf.micro.speaker.SpeakerGenerator;
import javaday.istanbul.sliconf.micro.sponsor.SponsorGenerator;
import javaday.istanbul.sliconf.micro.user.UserGenerator;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import javaday.istanbul.sliconf.micro.util.Constants;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.AdditionalMatchers.not;

@RequiredArgsConstructor
@Ignore
public class ListEventsTest extends SpringBootTestConfig {

    private final UserRepositoryService userRepositoryService;
    private final TokenAuthenticationService tokenAuthenticationService;
    private final AdminService adminService;
    private User adminUser;
    private Authentication authentication;
    private List<String> filters;
    private int totalEventCount;
    @Diyelimki("^Admin sistemdeki etkinlikleri görüntülemek istiyor$")
    public void adminSistemdekiEtkinlikleriGörüntülemekIstiyor() throws Throwable {

        adminUser = UserGenerator.generateRandomUsers(1).stream().findFirst().orElse(null);
        assertNotNull(adminUser);
        adminUser.setRole(Constants.ROLE_ADMIN);

        ResponseMessage userSaveResponseMessage = userRepositoryService.saveUser(adminUser);
        assertTrue(userSaveResponseMessage.getMessage(), userSaveResponseMessage.isStatus());

        authentication = tokenAuthenticationService
                .generateAuthentication(adminUser.getUsername(), adminUser.getRole(), adminUser);
    }

    @Eğerki("^Admin herhangi bir listeleme filtresi seçmediyse$")
    public void adminHerhangiBirListelemeFiltresiSeçmediyse() throws Throwable {
        filters = new ArrayList<>();
        filters.add("ACTIVE");
    }

    private Pageable pageable;
    @Ozaman("^Sistem aktif olan etkinlikleri bir sayfada (\\d+) adet olacak şekilde sayfalar halinde Admine gösterir$")
    public void sistemAktifOlanEtkinlikleriBirSayfadaAdetOlacakŞekildeSayfalarHalindeAdmineGösterir(int size) throws Throwable {

        pageable = new PageRequest(0, size);
        EnumSet<LifeCycleState.EventStatus> eventStatuses = EnumSet.allOf(LifeCycleState.EventStatus.class);
        eventStatuses.remove(LifeCycleState.EventStatus.ACTIVE);

        Page<Event> events = adminService.listEvents(filters, pageable);
        events.forEach(event -> assertThat(event.getLifeCycleState().getEventStatuses()
                , not(Matchers.contains(eventStatuses))));

    }

    @Eğerki("^Admin filtre olarak bir sayfada gösterilecek etkinlik sayısını (\\d+) sayfa numarasıni (\\d+) vermiş ise vermiş ise$")
    public void adminFiltreOlarakBirSayfadaGösterilecekEtkinlikSayısınıSayfaNumarasıniVermişIseVermişIse(int pageSize, int pageNumber) throws Throwable {
        pageable = new PageRequest(pageNumber, pageSize);
    }


    private final EventRepository eventRepository;
    private final EventRepositoryService eventRepositoryService;

    @Ve("^Sistemde aktif olan toplam (\\d+) etkinlik var ise$")
    public void sistemdeAktifOlanToplamEtkinlikVarIse(int eventCount) throws Throwable {
        eventRepository.deleteAll();
        Set<Event> eventSet = EventGenerator.generateRandomEvents(eventCount, adminUser.getId());
        eventSet.forEach(event -> {
            FloorGenerator.generateRandomFlors(1,event);
            RoomGenerator.generateRandomRooms(1, event);
            SpeakerGenerator.generateRandomSpeakers(15, event);
            AgendaGenerator.generateRandomAgendaElements(12, event);
            SponsorGenerator.generateRandomSponsors(10, event);
            EventSpecs.generateKanbanNumber(event,eventRepositoryService);
            eventRepositoryService.save(event);
        });

        totalEventCount = eventRepositoryService.findAll().size();

    }

    @Ozaman("^Sistem aktif olan etkinlikleri bir sayfada (\\d+) adet olacak şekilde (\\d+) sayfa halinde Admine gösterir$")
    public void sistemAktifOlanEtkinlikleriBirSayfadaAdetOlacakŞekildeSayfaHalindeAdmineGösterir(int pageSize, int pageNumber)
            throws Throwable {

        pageable = new PageRequest(pageNumber, pageSize);
        EnumSet<LifeCycleState.EventStatus> eventStatuses = EnumSet.allOf(LifeCycleState.EventStatus.class);
        eventStatuses.remove(LifeCycleState.EventStatus.ACTIVE);

        Page<Event> events = adminService.listEvents(filters, pageable);
        events.forEach(event ->
                assertThat(event.getLifeCycleState().getEventStatuses(), Matchers.not(Matchers.contains(eventStatuses))));
    }


    @Diyelimki("^Admin sistemdeki tüm etkinlikleri görüntülemek istiyor$")
    public void adminSistemdekiTümEtkinlikleriGörüntülemekIstiyor() throws Throwable {

        adminUser = UserGenerator.generateRandomUsers(1).stream().findFirst().orElse(null);
        assertNotNull(adminUser);
        adminUser.setRole(Constants.ROLE_ADMIN);

        ResponseMessage userSaveResponseMessage = userRepositoryService.saveUser(adminUser);
        assertTrue(userSaveResponseMessage.getMessage(), userSaveResponseMessage.isStatus());

        authentication = tokenAuthenticationService
                .generateAuthentication(adminUser.getUsername(), adminUser.getRole(), adminUser);

        Set<Event> eventSet = EventGenerator.generateRandomEvents(25, adminUser.getId());
        eventSet.forEach(event -> {
            EventSpecs.generateKanbanNumber(event, eventRepositoryService);
            eventRepositoryService.save(event);
        });
        totalEventCount = eventRepository.findAll().size();

    }

    @Eğerki("^Admin listeleme filtresi olarak tüm etkinlikleri seçtiyse$")
    public void adminListelemeFiltresiOlarakTümEtkinlikleriSeçtiyse() throws Throwable {

        filters = new ArrayList<>();
        filters.addAll(Stream.of(LifeCycleState.EventStatus.values())
                .map(LifeCycleState.EventStatus::name)
                .collect(Collectors.toList()));

    }

    @Ozaman("^Sistem bütün etkinlikleri Admine gösterir$")
    public void sistemBütünEtkinlikleriAdmineGösterir() throws Throwable {

        pageable = new PageRequest(0, 20);
        Page<Event> events = adminService.listEvents(filters, pageable);

        assertEquals(totalEventCount, events.getTotalElements());

    }

    @Diyelimki("^Admin sistemdeki aktif veya pasif  olan tüm etkinlikleri görüntülemek istiyor$")
    public void adminSistemdekiAktifVeyaPasifOlanTümEtkinlikleriGörüntülemekIstiyor() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Eğerki("^Admin listeleme filtresi olarak aktif veya pasif olan etkinlikleri seçti ise$")
    public void adminListelemeFiltresiOlarakAktifVeyaPasifOlanEtkinlikleriSeçtiIse() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Ozaman("^Sistem aktif veya pasif olan tüm etkinlikleri Admine gösterir$")
    public void sistemAktifVeyaPasifOlanTümEtkinlikleriAdmineGösterir() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Diyelimki("^Admin sistemdeki başarılı şekilde bitmiş olan tüm etkinlikleri görüntülemek istiyor$")
    public void adminSistemdekiBaşarılıŞekildeBitmişOlanTümEtkinlikleriGörüntülemekIstiyor() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Eğerki("^Admin listeleme filtresi olarak bitmiş etkinlikleri seçti ise$")
    public void adminListelemeFiltresiOlarakBitmişEtkinlikleriSeçtiIse() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Ozaman("^Sistem gerçekleşip bitmiş olan tüm etkinlikleri Admine gösterir$")
    public void sistemGerçekleşipBitmişOlanTümEtkinlikleriAdmineGösterir() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Diyelimki("^Admin sistemdeki aktif olamadan tamamlanıp başarısız etkinlikleri görüntülemek istiyor$")
    public void adminSistemdekiAktifOlamadanTamamlanıpBaşarısızEtkinlikleriGörüntülemekIstiyor() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Eğerki("^Admin listeleme filtresi olarak başarısız olmuş etkinlikleri seçmiş ise$")
    public void adminListelemeFiltresiOlarakBaşarısızOlmuşEtkinlikleriSeçmişIse() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Ozaman("^Sistem gerçekleşmeden bitip başarısız olan tüm etkinlikleri Admine gösterir$")
    public void sistemGerçekleşmedenBitipBaşarısızOlanTümEtkinlikleriAdmineGösterir() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Diyelimki("^Admin sistemdeki şu anda gerçekleşen etkinlikleri görüntülemek istiyor$")
    public void adminSistemdekiŞuAndaGerçekleşenEtkinlikleriGörüntülemekIstiyor() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Eğerki("^Admin listeleme filtresi olarak şu anda gerçekleşen etkinlikleri seçmiş ise$")
    public void adminListelemeFiltresiOlarakŞuAndaGerçekleşenEtkinlikleriSeçmişIse() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Ozaman("^Sistem şu anda gerçekleşen tüm etkinlikleri Admine gösterir$")
    public void sistemŞuAndaGerçekleşenTümEtkinlikleriAdmineGösterir() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }


}
