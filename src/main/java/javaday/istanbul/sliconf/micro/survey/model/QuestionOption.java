package javaday.istanbul.sliconf.micro.survey.model;

import javaday.istanbul.sliconf.micro.survey.validator.groups.SurveyQuestionOptionValidatorGroup;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
public class QuestionOption implements Serializable {

    @NotBlank(message = "{survey.question.questionOptions.id.blank}", groups = SurveyQuestionOptionValidatorGroup.class)
    private String id;

    @NotBlank(message = "{survey.question.questionOptions.text.blank}", groups = SurveyQuestionOptionValidatorGroup.class)
    private String text;

    private Integer voters;
}
