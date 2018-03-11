package javaday.istanbul.sliconf.micro.model.event.agenda;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "stars")
@CompoundIndexes(
        @CompoundIndex(def = "{'id':1}")
)
@Getter
@Setter
public class Star {
    @Id
    private String id;

    private String eventId;
    private String userId;
    private String sessionId;

    private int value;
}
