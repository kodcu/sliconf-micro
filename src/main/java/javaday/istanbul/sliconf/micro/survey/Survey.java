package javaday.istanbul.sliconf.micro.survey;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
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
    @NotBlank(message = "{survey.name.blank")
    private String name;
    private String userId;
    private String eventId;
    private String sessionId;
    private LocalDateTime time;
    private String description;
    @NotBlank
    private List<Question> questions;
    private List<Answer> answers;
}
