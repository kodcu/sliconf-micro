package javaday.istanbul.sliconf.micro.survey.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class QuestionOption {

    @NotBlank(message = "{survey.question.questionOptions.id.blank}")
    private String id;

    @NotBlank(message = "{survey.question.questionOptions.text.blank}")
    private String text;

    private Integer voters;
}
