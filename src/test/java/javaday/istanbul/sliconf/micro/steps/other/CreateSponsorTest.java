package javaday.istanbul.sliconf.micro.steps.other;


import cucumber.api.java.tr.Diyelimki;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.event.EventBuilder;
import javaday.istanbul.sliconf.micro.sponsor.CreateSponsorRoute;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.sponsor.Sponsor;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.event.service.EventRepositoryService;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import javaday.istanbul.sliconf.micro.event.EventSpecs;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Ignore
public class CreateSponsorTest extends SpringBootTestConfig { // NOSONAR

    @Autowired
    UserRepositoryService userRepositoryService;

    @Autowired
    EventRepositoryService eventRepositoryService;

    @Autowired
    CreateSponsorRoute createSponsorRoute;


    @Diyelimki("^Sponsor kaydediliyor$")
    public void sponsorKaydediliyor() throws Throwable {
        // Given
        User user = new User();
        user.setUsername("createSponsorUser1");
        user.setEmail("createSponsorUser1@sliconf.com");
        user.setPassword("123123123");

        ResponseMessage savedUserMessage = userRepositoryService.saveUser(user);

        assertTrue(savedUserMessage.isStatus());

        String userId = ((User) savedUserMessage.getReturnObject()).getId();

        Event event = new EventBuilder().setName("Create Sponsor Test")
                .setExecutiveUser(userId)
                .setDate(LocalDateTime.now().plusMonths(2)).build();

        EventSpecs.generateKanbanNumber(event, eventRepositoryService);
        event.setStatus(true);

        ResponseMessage eventSaveMessage = eventRepositoryService.save(event);
        String eventKey = ((Event) eventSaveMessage.getReturnObject()).getKey();

        Map<String, List<Sponsor>> sponsorMap = createSponsors();
        Map<String, String> sponsorTagMap = createSponsorTags();


        // When

        // True
        ResponseMessage saveSponsorsResponseMessage1 = createSponsorRoute.saveSponsorsAndSponsorTags(sponsorMap, sponsorTagMap, eventKey);

        // False
        ResponseMessage saveSponsorsResponseMessage2 = createSponsorRoute.saveSponsorsAndSponsorTags(sponsorMap, sponsorTagMap, "");
        ResponseMessage saveSponsorsResponseMessage3 = createSponsorRoute.saveSponsorsAndSponsorTags(sponsorMap, sponsorTagMap, null);
        ResponseMessage saveSponsorsResponseMessage4 = createSponsorRoute.saveSponsorsAndSponsorTags(null, sponsorTagMap, eventKey);
        ResponseMessage saveSponsorsResponseMessage5 = createSponsorRoute.saveSponsorsAndSponsorTags(sponsorMap, null, null);
        ResponseMessage saveSponsorsResponseMessage6 = createSponsorRoute.saveSponsorsAndSponsorTags(null, null, eventKey);

        // Then
        assertTrue(saveSponsorsResponseMessage1.isStatus());

        assertFalse(saveSponsorsResponseMessage2.isStatus());
        assertFalse(saveSponsorsResponseMessage3.isStatus());
        assertFalse(saveSponsorsResponseMessage4.isStatus());
        assertFalse(saveSponsorsResponseMessage5.isStatus());
        assertFalse(saveSponsorsResponseMessage6.isStatus());

    }

    private Map<String, List<Sponsor>> createSponsors() {

        Map<String, List<Sponsor>> sponsorMap = new HashMap<>();

        List<Sponsor> sponsors1 = new ArrayList<>();
        List<Sponsor> sponsors2 = new ArrayList<>();

        Sponsor sponsor1 = new Sponsor();
        sponsor1.setId("sponsor1");
        sponsor1.setLogo("logo1");
        sponsor1.setName("sponsor1");

        Sponsor sponsor2 = new Sponsor();
        sponsor2.setId("sponsor2");
        sponsor2.setLogo("logo2");
        sponsor2.setName("sponsor2");

        Sponsor sponsor3 = new Sponsor();
        sponsor3.setId("sponsor3");
        sponsor3.setLogo("logo3");
        sponsor3.setName("sponsor3");

        sponsors1.add(sponsor1);
        sponsors1.add(sponsor2);

        sponsors2.add(sponsor3);

        sponsorMap.put("newid1", sponsors1);
        sponsorMap.put("newid2", sponsors2);

        return sponsorMap;
    }

    private Map<String, String> createSponsorTags() {
        Map<String, String> sponsorTagMap = new HashMap<>();

        sponsorTagMap.put("newid1", "gold");
        sponsorTagMap.put("newid2", "diamond");

        return sponsorTagMap;
    }
}
