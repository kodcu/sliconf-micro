package javaday.istanbul.sliconf.micro.steps.other;


import cucumber.api.java.tr.Diyelimki;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.agenda.model.AgendaElement;
import javaday.istanbul.sliconf.micro.event.EventBuilder;
import javaday.istanbul.sliconf.micro.event.EventSpecs;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.event.model.LifeCycleState;
import javaday.istanbul.sliconf.micro.event.service.EventRepositoryService;
import javaday.istanbul.sliconf.micro.floor.Floor;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.room.Room;
import javaday.istanbul.sliconf.micro.speaker.CreateSpeakerRoute;
import javaday.istanbul.sliconf.micro.speaker.Speaker;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import javaday.istanbul.sliconf.micro.util.TestUtil;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Ignore
public class AddSpeakerTest extends SpringBootTestConfig { // NOSONAR


    @Autowired
    UserRepositoryService userRepositoryService;

    @Autowired
    EventRepositoryService eventRepositoryService;

    @Autowired
    CreateSpeakerRoute createSpeakerRoute;


    private List<Speaker> speakers = new ArrayList<>();
    private List<AgendaElement> agendaElements = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private List<Floor> floors = new ArrayList<>();

    @Diyelimki("^Kullanici speaker eklemek istiyor$")
    public void kullaniciSpeakerEklemekIstiyor() throws Throwable {
        // Given
        User user = new User();
        user.setUsername("speakerAddUser");
        user.setEmail("speakerAddUser@sliconf.com");
        user.setPassword("123123123");

        ResponseMessage savedUserMessage = userRepositoryService.saveUser(user);

        assertTrue(savedUserMessage.isStatus());

        String userId = ((User) savedUserMessage.getReturnObject()).getId();

        Event event = new EventBuilder().setName("Speaker Test Event")
                .setExecutiveUser(userId)
                .setDate(LocalDateTime.now().plusMonths(2)).build();

        EventSpecs.generateKanbanNumber(event, eventRepositoryService);

        TestUtil.generateFields(floors, rooms, speakers, agendaElements);

        event.setStatus(true);

        event.setFloorPlan(floors);
        event.setRooms(rooms);
        event.setSpeakers(speakers);
        event.setAgenda(agendaElements);
        LifeCycleState lifeCycleState = new LifeCycleState();
        lifeCycleState.setEventStatuses(new HashSet<>());
        event.setLifeCycleState(lifeCycleState);


        ResponseMessage eventSaveMessage = eventRepositoryService.save(event);
        String eventKey = ((Event) eventSaveMessage.getReturnObject()).getKey();

        // When

        ResponseMessage saveSpeakersResponseMessage1 = createSpeakerRoute.saveSpeakers(generateSpeaker1(), eventKey);
        ResponseMessage saveSpeakersResponseMessage2 = createSpeakerRoute.saveSpeakers(generateSpeaker2(), eventKey);

        ResponseMessage saveSpeakersResponseMessage3 = createSpeakerRoute.saveSpeakers(generateSpeaker2(), null);
        ResponseMessage saveSpeakersResponseMessage4 = createSpeakerRoute.saveSpeakers(generateSpeaker2(), "sfsdf");
        ResponseMessage saveSpeakersResponseMessage5 = createSpeakerRoute.saveSpeakers(null, eventKey);
        ResponseMessage saveSpeakersResponseMessage6 = createSpeakerRoute.saveSpeakers(generateSpeaker3(), eventKey);


        // Then
        assertTrue(saveSpeakersResponseMessage1.isStatus());
        assertTrue(saveSpeakersResponseMessage2.isStatus());

        assertFalse(saveSpeakersResponseMessage3.isStatus());
        assertFalse(saveSpeakersResponseMessage4.isStatus());
        assertFalse(saveSpeakersResponseMessage5.isStatus());
        assertFalse(saveSpeakersResponseMessage6.isStatus());
    }

    private List<Speaker> generateSpeaker1() {
        List<Speaker> speakers = new ArrayList<>();

        Speaker speaker1 = new Speaker();
        speaker1.setName("Osman Osman");
        speaker1.setId("newid1");
        speaker1.setAbout("Yeni bir konusmaci gibisi yok");
        speaker1.setLinkedin("linkedin 1");
        speaker1.setTwitter("twitter1");
        speaker1.setProfilePicture("profile-picture-1");
        speaker1.setWorkingAt("kodcu1");

        Speaker speaker2 = new Speaker();
        speaker2.setName("Osman Osman");
        speaker2.setId("newid2");
        speaker2.setAbout("Yeni bir konusmaci gibisi yok");
        speaker2.setLinkedin("linkedin 1");
        speaker2.setTwitter("twitter1");
        speaker2.setProfilePicture("profile-picture-1");
        speaker2.setWorkingAt("kodcu1");

        Speaker speaker3 = new Speaker();
        speaker3.setName("Osman Osman");
        speaker3.setId("newid3");
        speaker3.setAbout("Yeni bir konusmaci gibisi yok");
        speaker3.setLinkedin("linkedin 1");
        speaker3.setTwitter("twitter1");
        speaker3.setProfilePicture("profile-picture-1");
        speaker3.setWorkingAt("kodcu1");

        Speaker speaker4 = new Speaker();
        speaker4.setName("Osman Osman");
        speaker4.setId("newid4");
        speaker4.setAbout("Yeni bir konusmaci gibisi yok");
        speaker4.setLinkedin("linkedin 1");
        speaker4.setTwitter("twitter1");
        speaker4.setProfilePicture("profile-picture-1");
        speaker4.setWorkingAt("kodcu1");

        speakers.add(speaker1);
        speakers.add(speaker2);
        speakers.add(speaker3);
        speakers.add(speaker4);

        return speakers;
    }

    private List<Speaker> generateSpeaker2() {
        List<Speaker> speakers = new ArrayList<>();

        Speaker speaker1 = new Speaker();
        speaker1.setName("Osman Osman");
        speaker1.setId("asdasdasd");
        speaker1.setAbout("Yeni bir konusmaci gibisi yok");
        speaker1.setLinkedin("linkedin 1");
        speaker1.setTwitter("twitter1");
        speaker1.setProfilePicture("profile-picture-1");
        speaker1.setWorkingAt("kodcu1");

        Speaker speaker2 = new Speaker();
        speaker2.setName("Osman Osman");
        speaker2.setId("asdasdaq");
        speaker2.setAbout("Yeni bir konusmaci gibisi yok");
        speaker2.setLinkedin("linkedin 1");
        speaker2.setTwitter("twitter1");
        speaker2.setProfilePicture("profile-picture-1");
        speaker2.setWorkingAt("kodcu1");

        Speaker speaker3 = new Speaker();
        speaker3.setName("Osman Osman");
        speaker3.setId("newid1");
        speaker3.setAbout("Yeni bir konusmaci gibisi yok");
        speaker3.setLinkedin("linkedin 1");
        speaker3.setTwitter("twitter1");
        speaker3.setProfilePicture("profile-picture-1");
        speaker3.setWorkingAt("kodcu1");

        Speaker speaker4 = new Speaker();
        speaker4.setName("Osman Osman");
        speaker4.setId("newid2");
        speaker4.setAbout("Yeni bir konusmaci gibisi yok");
        speaker4.setLinkedin("linkedin 1");
        speaker4.setTwitter("twitter1");
        speaker4.setProfilePicture("profile-picture-1");
        speaker4.setWorkingAt("kodcu1");

        speakers.add(speaker1);
        speakers.add(speaker2);
        speakers.add(speaker3);
        speakers.add(speaker4);

        return speakers;
    }

    private List<Speaker> generateSpeaker3() {
        List<Speaker> speakers = new ArrayList<>();

        Speaker speaker1 = new Speaker();
        speaker1.setName("Osman Osman");
        speaker1.setId("asdasdasd");
        speaker1.setAbout("Yeni bir konusmaci gibisi yok");
        speaker1.setLinkedin("linkedin 1");
        speaker1.setTwitter("twitter1");
        speaker1.setProfilePicture("profile-picture-1");
        speaker1.setWorkingAt("kodcu1");


        Speaker speaker4 = null;


        speakers.add(speaker1);
        speakers.add(speaker4);

        return speakers;
    }
}
