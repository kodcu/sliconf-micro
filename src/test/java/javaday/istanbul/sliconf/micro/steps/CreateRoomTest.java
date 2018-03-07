package javaday.istanbul.sliconf.micro.steps;


import cucumber.api.java.tr.Diyelimki;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.builder.EventBuilder;
import javaday.istanbul.sliconf.micro.controller.event.room.CreateRoomRoute;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.event.Floor;
import javaday.istanbul.sliconf.micro.model.event.Room;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.specs.EventSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@ContextConfiguration(classes = {CucumberConfiguration.class})
@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class CreateRoomTest {// NOSONAR

    @Autowired
    UserRepositoryService userRepositoryService;

    @Autowired
    EventRepositoryService eventRepositoryService;

    @Autowired
    CreateRoomRoute createRoomRoute;


    @Diyelimki("^Room kaydediliyor$")
    public void roomKaydediliyor() throws Throwable {
        // Given
        User user = new User();
        user.setUsername("createRoomUser1");
        user.setEmail("createRoomUser1@sliconf.com");
        user.setPassword("123123123");

        ResponseMessage savedUserMessage = userRepositoryService.saveUser(user);

        assertTrue(savedUserMessage.isStatus());

        String userId = ((User) savedUserMessage.getReturnObject()).getId();

        Event event = new EventBuilder().setName("Create Room Test")
                .setExecutiveUser(userId)
                .setDate(LocalDateTime.now().plusMonths(2)).build();

        EventSpecs.generateKanbanNumber(event, eventRepositoryService);
        event.setStatus(true);

        event.setFloorPlan(createFLoors1());

        ResponseMessage eventSaveMessage = eventRepositoryService.save(event);
        String eventKey = ((Event) eventSaveMessage.getReturnObject()).getKey();


        List<Room> rooms1 = createRooms1();

        // When

        // True
        ResponseMessage saveRoomResponseMessage1 = createRoomRoute.saveRooms(rooms1, eventKey);

        // False
        ResponseMessage saveRoomResponseMessage2 = createRoomRoute.saveRooms(rooms1, null);
        ResponseMessage saveRoomResponseMessage3 = createRoomRoute.saveRooms(rooms1, "9999");
        ResponseMessage saveRoomResponseMessage4 = createRoomRoute.saveRooms(rooms1, "");
        ResponseMessage saveRoomResponseMessage5 = createRoomRoute.saveRooms(null, "");


        // Then
        assertTrue(saveRoomResponseMessage1.isStatus());

        assertFalse(saveRoomResponseMessage2.isStatus());
        assertFalse(saveRoomResponseMessage3.isStatus());
        assertFalse(saveRoomResponseMessage4.isStatus());
        assertFalse(saveRoomResponseMessage5.isStatus());
    }

    private List<Room> createRooms1() {
        List<Room> roomList = new ArrayList<>();

        Room room1 = new Room();
        room1.setFloor("floor12");
        room1.setId("room12");
        room1.setLabel("Room 12");

        Room room2 = new Room();
        room2.setFloor("floor2");
        room2.setId("room2");
        room2.setLabel("Room 2");

        Room room3 = new Room();
        room3.setFloor("floor3");
        room3.setId("room3");
        room3.setLabel("Room 3");

        roomList.add(room1);
        roomList.add(room2);
        roomList.add(room2);

        return roomList;
    }

    private List<Floor> createFLoors1() {
        List<Floor> floors = new ArrayList<>();

        Floor floor1 = new Floor();
        floor1.setId("floor12");
        floor1.setName("Floor 12");
        floor1.setImage("floorImage12");

        Floor floor2 = new Floor();
        floor2.setId("floor2");
        floor2.setName("Floor 2");
        floor2.setImage("floorImage2");

        Floor floor3 = new Floor();
        floor3.setId("floor3");
        floor3.setName("Floor 3");
        floor3.setImage("floorImage3");

        floors.add(floor1);
        floors.add(floor2);
        floors.add(floor3);

        return floors;
    }

}
