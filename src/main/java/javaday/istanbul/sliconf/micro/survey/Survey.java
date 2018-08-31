package javaday.istanbul.sliconf.micro.survey;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;


@Document(collection = "surveys")
@CompoundIndexes(
        @CompoundIndex(def = "{'id':1}")
)
@Getter
@Setter
class Survey {

    @Id
    private String id;
    @NotBlank(message = "{survey.name.blank}")
    private String name;
    @NotBlank(message = "{survey.userId.blank}")
    private String userId;
    @NotBlank(message = "{survey.eventId.blank}")
    private String eventId;
    @NotBlank(message = "{survey.sessionId.blank}")
    private String sessionId;
    private LocalDateTime time;
    private String description;

    private List<Question> questions;
    private List<Answer> answers;
}
