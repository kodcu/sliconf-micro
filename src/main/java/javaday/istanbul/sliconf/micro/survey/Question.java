package javaday.istanbul.sliconf.micro.survey;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class Question {

    @NotBlank
    int order;
    @NotBlank
    private String text;
    @NotBlank
    @Size(max = 4)
    private List<String> options;

}
