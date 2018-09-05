package javaday.istanbul.sliconf.micro.survey.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.NumberFormat;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;


@Getter
@Setter

public class Question {

    @Id
    @NotBlank(message = "{survey.question.id.blank}")
    private String id;

    @NotBlank(message = "{survey.question.text.blank}")
    private String text;

    @NotNull
    private Integer totalVoters;


    @Size(min = 2, message = "{survey.question.questionOptions.size}")
    private List<QuestionOption> options;
}
