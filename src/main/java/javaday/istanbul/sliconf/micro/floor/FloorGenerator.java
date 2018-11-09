package javaday.istanbul.sliconf.micro.floor;

import javaday.istanbul.sliconf.micro.event.model.Event;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class FloorGenerator {

    private static int floorNumber = 0;

    public static void generateRandomFlors(int count, Event event) {

        List<Floor> floors = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Floor floor = Floor.builder()
                    .name("Floor " + (floorNumber++ + 1))
                    .id(new ObjectId().toString())
                    .build();
            floors.add(floor);
        }
        event.setFloorPlan(floors);

    }
}
