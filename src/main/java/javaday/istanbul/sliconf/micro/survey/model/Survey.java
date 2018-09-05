package javaday.istanbul.sliconf.micro.survey.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
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
public class Survey {

    @Id
    private String id;

    @NotBlank(message = "{survey.name.blank}")
    private String name;

    @NotBlank(message = "{survey.userId.blank}")
    private String userId;

    @NotBlank(message = "{survey.eventKey.blank}")
    private String eventKey;

//    @NotBlank(message = "{survey.sessionId.blank}")
//    private String sessionId;

    private LocalDateTime time;

    private String description;

    private Integer viewers;

    private Integer participants;

    @NotEmpty(message = "{survey.questions.empty}")
    private List<Question> questions;
}
