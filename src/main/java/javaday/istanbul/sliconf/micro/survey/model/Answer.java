package javaday.istanbul.sliconf.micro.survey.model;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Map;

/**
 * Bir kullanıcının bir ankete verdiği cevaplara karşılık gelen model nesnesi.
 */

@Document(collection = "surveyAnswers")
@CompoundIndexes(
        @CompoundIndex(def = "{'id':1}")
)
@Getter
@Setter
public class Answer implements Serializable {

    @Id
    @NotBlank(message = "{survey.answer.id.blank}")
    private String id;

    @NotBlank(message = "{survey.answer.surveyId.blank}")
    private String surveyId;

    @NotBlank(message = "{survey.answer.userId.blank}")
    private String userId;

    @NotBlank(message = "{survey.answer.eventId.blank}")
    private String eventId;

    @NotBlank(message = "{survey.answer.eventKey.blank}")
    private String eventKey;

    //burada key question id, value ise verdigi cevaptir.
    @NotEmpty(message = "survey.answers.empty")
    private Map<String, String> answeredQuestions;
}
