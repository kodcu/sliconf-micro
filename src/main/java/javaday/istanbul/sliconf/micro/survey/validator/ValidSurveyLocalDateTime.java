package javaday.istanbul.sliconf.micro.survey.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = { SurveyLocalDateTimeValidator.class })
@Documented
public @interface ValidSurveyLocalDateTime {
    String message() default "{javaday.istanbul.sliconf.micro." +
            "Invalid start and end date.}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
