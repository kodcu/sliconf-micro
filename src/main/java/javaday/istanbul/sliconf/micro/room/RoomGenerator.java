package javaday.istanbul.sliconf.micro.room;

import com.devskiller.jfairy.Fairy;
import javaday.istanbul.sliconf.micro.event.model.Event;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class RoomGenerator {

    private static int roomNumber = 1;

    public static void generateRandomRooms(int count, Event event) {
        Fairy fairy = Fairy.create();
        List<Room> rooms = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Room room = Room.builder()
                    .floor(fairy.baseProducer().randomElement(event.getFloorPlan()).getId())
                    .id(new ObjectId().toString())
                    .label("Room " + (roomNumber++ + 1))
                    .build();

            rooms.add(room);

        }
        event.setRooms(rooms);
    }

}
