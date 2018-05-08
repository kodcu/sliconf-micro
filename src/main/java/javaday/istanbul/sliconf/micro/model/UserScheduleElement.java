package javaday.istanbul.sliconf.micro.model;

import javaday.istanbul.sliconf.micro.model.event.Floor;
import javaday.istanbul.sliconf.micro.model.event.Room;
import javaday.istanbul.sliconf.micro.model.event.Speaker;
import javaday.istanbul.sliconf.micro.model.event.agenda.AgendaElement;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by ttayfur on 13/3/17.
 *
 * Kullanicinin katilacagi etkinligi tutan nesne
 */
@Document(collection = "userSchedules")
@CompoundIndexes(@CompoundIndex(def = "{'id':1}"))
@Getter
@Setter
public class UserScheduleElement {
    @Id
    private String id;
    private String eventId;
    private String sessionId;
    private String userId;

    private AgendaElement agendaElement;
    private Speaker speaker;
    private Room room;
    private Floor floor;
}
