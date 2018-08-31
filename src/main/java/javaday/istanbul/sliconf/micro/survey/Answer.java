package javaday.istanbul.sliconf.micro.survey;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;
/**
 * Bir kullanıcının bir ankete verdiği cevaplara karşılık gelen model nesnesi.
 */
@Getter
@Setter
public class Answer {

    @NotBlank
    private String userId;

    @NotEmpty(message = "survey.answers.empty")
    Map<Integer, Integer> answers;
}
