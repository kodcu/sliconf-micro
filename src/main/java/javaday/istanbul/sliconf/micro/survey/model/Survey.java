package javaday.istanbul.sliconf.micro.survey.model;

import javaday.istanbul.sliconf.micro.survey.validator.ValidStartAndEndLocalDateTime;
import javaday.istanbul.sliconf.micro.survey.validator.groups.SurveyQuestionValidatorGroup;
import javaday.istanbul.sliconf.micro.survey.validator.groups.SurveyStartAndEndLocalDateTimeValidatorGroup;
import javaday.istanbul.sliconf.micro.survey.validator.groups.SurveyValidatorGroup;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;


@Document(collection = "surveys")
@CompoundIndexes(
        @CompoundIndex(def = "{'id':1}")
)
@Getter
@Setter

// anketin baslangic ve bitis tarihini kontrol icin custom validation.
@ValidStartAndEndLocalDateTime(groups = SurveyStartAndEndLocalDateTimeValidatorGroup.class)
public class Survey implements Serializable {

    @Id
    private String id;

    @NotBlank(message = "{survey.name.blank}", groups = SurveyValidatorGroup.class)
    private String name;

    @NotBlank(message = "{survey.userId.blank}", groups = SurveyValidatorGroup.class)
    private String userId;

    @NotBlank(message = "{survey.eventId.blank}", groups = SurveyValidatorGroup.class)
    private String eventId;

    @Pattern(regexp = "^\\d+\\d$", groups = SurveyValidatorGroup.class, message = "{survey.localDateTime.invalid}")
    private String startTime;

    @Pattern(regexp = "^\\d+\\d$", groups = SurveyValidatorGroup.class)
    private String endTime;

    private String description;

    private Integer viewers;

    private Integer participants;

    @NotEmpty(message = "{survey.questions.empty}", groups = SurveyQuestionValidatorGroup.class)
    private List<Question> questions;
}
