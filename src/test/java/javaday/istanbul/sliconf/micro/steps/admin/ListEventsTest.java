package javaday.istanbul.sliconf.micro.steps.admin;

import com.devskiller.jfairy.Fairy;
import com.google.api.client.util.Lists;
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
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@RequiredArgsConstructor
@Ignore
public class ListEventsTest extends SpringBootTestConfig { // NOSONAR

    private final UserRepositoryService userRepositoryService;
    private final TokenAuthenticationService tokenAuthenticationService;
    private final AdminService adminService;
    private final EventRepository eventRepository;
    private final EventRepositoryService eventRepositoryService;
    private User adminUser;
    private Authentication authentication;
    private List<String> filters;
    private int expectedEventCount;
    private Pageable pageable;

    private void generateAdminUser() {

        adminUser = UserGenerator.generateRandomUsers(1).stream().findFirst().orElse(null);
        assertNotNull(adminUser);
        adminUser.setRole(Constants.ROLE_ADMIN);

        ResponseMessage userSaveResponseMessage = userRepositoryService.saveUser(adminUser);
        assertTrue(userSaveResponseMessage.getMessage(), userSaveResponseMessage.isStatus());

        authentication = tokenAuthenticationService
                .generateAuthentication(adminUser.getUsername(), adminUser.getRole(), adminUser);
    }

    private void generateActiveEvent(Set<Event> eventSet) {
        eventSet.forEach(event -> {
            FloorGenerator.generateRandomFlors(1, event);
            RoomGenerator.generateRandomRooms(1, event);
            SpeakerGenerator.generateRandomSpeakers(15, event);
            AgendaGenerator.generateRandomAgendaElements(12, event);
            SponsorGenerator.generateRandomSponsors(10, event);
            EventSpecs.generateKanbanNumber(event, eventRepositoryService);
            eventRepositoryService.save(event);
        });
    }

    @Diyelimki("^Admin sistemdeki etkinlikleri görüntülemek istiyor$")
    public void adminSistemdekiEtkinlikleriGörüntülemekIstiyor() throws Throwable {
        generateAdminUser();
        Set<Event> eventSet = EventGenerator.generateRandomEvents(25, adminUser.getId());
        generateActiveEvent(eventSet);
    }

    @Eğerki("^Admin herhangi bir listeleme filtresi seçmediyse$")
    public void adminHerhangiBirListelemeFiltresiSeçmediyse() throws Throwable {
        filters = new ArrayList<>();
        filters.add("ACTIVE");
    }

    @Ozaman("^Sistem aktif olan etkinlikleri bir sayfada (\\d+) adet olacak şekilde sayfalar halinde Admine gösterir$")
    public void sistemAktifOlanEtkinlikleriBirSayfadaAdetOlacakŞekildeSayfalarHalindeAdmineGösterir(int size) throws Throwable {

        pageable = new PageRequest(0, size);
        List<LifeCycleState.EventStatus> eventStatuses = new ArrayList<>();
        for (String lifeCycleState1 : filters)
            eventStatuses.add(LifeCycleState.EventStatus.valueOf(lifeCycleState1));
        eventStatuses.clear();
        eventStatuses.add(LifeCycleState.EventStatus.ACTIVE);

        Page<Event> events = adminService.listEvents(filters, pageable);
        for (Event event : events) {
            assertThat(event.getLifeCycleState().getEventStatuses(),
                    Matchers.hasItems(Matchers.isOneOf(eventStatuses.get(0))));
        }

    }

    @Eğerki("^Admin filtre olarak bir sayfada gösterilecek etkinlik sayısını (\\d+) sayfa numarasıni (\\d+) vermiş ise vermiş ise$")
    public void adminFiltreOlarakBirSayfadaGösterilecekEtkinlikSayısınıSayfaNumarasıniVermişIseVermişIse(int pageSize, int pageNumber) throws Throwable {
        pageable = new PageRequest(pageNumber, pageSize);
    }

    @Ve("^Sistemde aktif olan toplam (\\d+) etkinlik var ise$")
    public void sistemdeAktifOlanToplamEtkinlikVarIse(int eventCount) throws Throwable {
        eventRepository.deleteAll();
        Set<Event> eventSet = EventGenerator.generateRandomEvents(eventCount, adminUser.getId());
        generateActiveEvent(eventSet);

        expectedEventCount = eventRepositoryService.findAll().size();

    }

    @Ozaman("^Sistem aktif olan etkinlikleri bir sayfada (\\d+) adet olacak şekilde (\\d+) sayfa halinde Admine gösterir$")
    public void sistemAktifOlanEtkinlikleriBirSayfadaAdetOlacakŞekildeSayfaHalindeAdmineGösterir(int pageSize, int pageNumber)
            throws Throwable {

        pageable = new PageRequest(pageNumber, pageSize);
        List<LifeCycleState.EventStatus> eventStatuses = new ArrayList<>();
        for (String lifeCycleState1 : filters)
            eventStatuses.add(LifeCycleState.EventStatus.valueOf(lifeCycleState1));
        eventStatuses.clear();
        eventStatuses.add(LifeCycleState.EventStatus.ACTIVE);

        Page<Event> events = adminService.listEvents(filters, pageable);
        for (Event event : events) {
            assertThat(event.getLifeCycleState().getEventStatuses(),
                    Matchers.hasItems(Matchers.isOneOf(eventStatuses.get(0))));
        }
    }


    @Diyelimki("^Admin sistemdeki tüm etkinlikleri görüntülemek istiyor$")
    public void adminSistemdekiTümEtkinlikleriGörüntülemekIstiyor() throws Throwable {

        generateAdminUser();

        Fairy fairy = Fairy.create();
        List<LifeCycleState.EventStatus> eventStatuses = Lists.newArrayList(Arrays.asList(LifeCycleState.EventStatus.values()));

        // test icin rastgele etkinlikler ekliyoruz.active ve passive eventler zaten var.
        eventStatuses.remove(LifeCycleState.EventStatus.PASSIVE);
        eventStatuses.remove(LifeCycleState.EventStatus.ACTIVE);
        Set<Event> eventSet = EventGenerator.generateRandomEvents(50, adminUser.getId());
        eventSet.forEach(event -> {
            EventSpecs.generateKanbanNumber(event, eventRepositoryService);
            event.getLifeCycleState()
                    .setEventStatuses(fairy.baseProducer()
                            .randomElements(Lists.newArrayList(eventStatuses), 1));
            eventRepositoryService.save(event);
        });
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

        expectedEventCount = eventRepository.findAll().size();
        pageable = new PageRequest(0, 20);
        Page<Event> events = adminService.listEvents(filters, pageable);

        assertEquals(expectedEventCount, events.getTotalElements());

    }

    @Diyelimki("^Admin sistemdeki aktif veya pasif olan tüm etkinlikleri görüntülemek istiyor$")
    public void adminSistemdekiAktifVeyaPasifOlanTümEtkinlikleriGörüntülemekIstiyor() throws Throwable {

        generateAdminUser();
    }

    @Eğerki("^Admin listeleme filtresi olarak aktif veya pasif olan etkinlikleri seçti ise$")
    public void adminListelemeFiltresiOlarakAktifVeyaPasifOlanEtkinlikleriSeçtiIse() throws Throwable {
        filters = new ArrayList<>();
        filters.add("ACTIVE");
        filters.add("PASSIVE");
    }

    @Ozaman("^Sistem aktif veya pasif olan tüm etkinlikleri Admine gösterir$")
    public void sistemAktifVeyaPasifOlanTümEtkinlikleriAdmineGösterir() throws Throwable {

        pageable = new PageRequest(0, 20);
        Page<Event> events = adminService.listEvents(filters, pageable);

        List<LifeCycleState.EventStatus> eventStatuses = new ArrayList<>();
        for (String lifeCycleState1 : filters)
            eventStatuses.add(LifeCycleState.EventStatus.valueOf(lifeCycleState1));
        eventStatuses.clear();
        eventStatuses.add(LifeCycleState.EventStatus.ACTIVE);
        eventStatuses.add(LifeCycleState.EventStatus.PASSIVE);

        for (Event event : events) {
            assertThat(event.getLifeCycleState().getEventStatuses(),
                    Matchers.hasItems(Matchers.isOneOf(eventStatuses.get(0), eventStatuses.get(1))));
        }
    }

    @Diyelimki("^Admin sistemdeki başarılı şekilde bitmiş olan tüm etkinlikleri görüntülemek istiyor$")
    public void adminSistemdekiBaşarılıŞekildeBitmişOlanTümEtkinlikleriGörüntülemekIstiyor() throws Throwable {
        generateAdminUser();
    }

    @Eğerki("^Admin listeleme filtresi olarak bitmiş etkinlikleri seçti ise$")
    public void adminListelemeFiltresiOlarakBitmişEtkinlikleriSeçtiIse() throws Throwable {
        filters = new ArrayList<>();
        filters.add("FINISHED");
    }

    @Ozaman("^Sistem gerçekleşip bitmiş olan tüm etkinlikleri Admine gösterir$")
    public void sistemGerçekleşipBitmişOlanTümEtkinlikleriAdmineGösterir() throws Throwable {
        pageable = new PageRequest(0, 20);
        Page<Event> events = adminService.listEvents(filters, pageable);

        List<LifeCycleState.EventStatus> eventStatuses = new ArrayList<>();
        for (String lifeCycleState1 : filters)
            eventStatuses.add(LifeCycleState.EventStatus.valueOf(lifeCycleState1));
        eventStatuses.clear();
        eventStatuses.add(LifeCycleState.EventStatus.FINISHED);

        for (Event event : events) {
            assertThat(event.getLifeCycleState().getEventStatuses(),
                    Matchers.hasItem(eventStatuses.get(0)));
        }
    }

    @Diyelimki("^Admin sistemdeki aktif olamadan tamamlanıp başarısız etkinlikleri görüntülemek istiyor$")
    public void adminSistemdekiAktifOlamadanTamamlanıpBaşarısızEtkinlikleriGörüntülemekIstiyor() throws Throwable {
        generateAdminUser();
    }

    @Eğerki("^Admin listeleme filtresi olarak başarısız olmuş etkinlikleri seçmiş ise$")
    public void adminListelemeFiltresiOlarakBaşarısızOlmuşEtkinlikleriSeçmişIse() throws Throwable {
        filters = new ArrayList<>();
        filters.add("FAILED");
    }

    @Ozaman("^Sistem gerçekleşmeden bitip başarısız olan tüm etkinlikleri Admine gösterir$")
    public void sistemGerçekleşmedenBitipBaşarısızOlanTümEtkinlikleriAdmineGösterir() throws Throwable {
        pageable = new PageRequest(0, 20);
        Page<Event> events = adminService.listEvents(filters, pageable);

        List<LifeCycleState.EventStatus> eventStatuses = new ArrayList<>();
        for (String lifeCycleState1 : filters)
            eventStatuses.add(LifeCycleState.EventStatus.valueOf(lifeCycleState1));
        eventStatuses.clear();
        eventStatuses.add(LifeCycleState.EventStatus.FAILED);

        for (Event event : events) {
            assertThat(event.getLifeCycleState().getEventStatuses(),
                    Matchers.hasItem(eventStatuses.get(0)));
        }
    }

    @Diyelimki("^Admin sistemdeki şu anda gerçekleşen etkinlikleri görüntülemek istiyor$")
    public void adminSistemdekiŞuAndaGerçekleşenEtkinlikleriGörüntülemekIstiyor() throws Throwable {
        generateAdminUser();
    }

    @Eğerki("^Admin listeleme filtresi olarak şu anda gerçekleşen etkinlikleri seçmiş ise$")
    public void adminListelemeFiltresiOlarakŞuAndaGerçekleşenEtkinlikleriSeçmişIse() throws Throwable {
        filters = new ArrayList<>();
        filters.add("HAPPENING");
    }

    @Ozaman("^Sistem şu anda gerçekleşen tüm etkinlikleri Admine gösterir$")
    public void sistemŞuAndaGerçekleşenTümEtkinlikleriAdmineGösterir() throws Throwable {
        pageable = new PageRequest(0, 20);
        Page<Event> events = adminService.listEvents(filters, pageable);

        List<LifeCycleState.EventStatus> eventStatuses = new ArrayList<>();
        for (String lifeCycleState1 : filters)
            eventStatuses.add(LifeCycleState.EventStatus.valueOf(lifeCycleState1));
        eventStatuses.clear();
        eventStatuses.add(LifeCycleState.EventStatus.HAPPENING);

        for (Event event : events) {
            assertThat(event.getLifeCycleState().getEventStatuses(),
                    Matchers.hasItem(eventStatuses.get(0)));
        }
    }


}
