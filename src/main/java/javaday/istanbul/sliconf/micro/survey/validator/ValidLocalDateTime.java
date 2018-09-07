package javaday.istanbul.sliconf.micro.survey.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {LocalDateTimeValidator.class})
@Documented
public @interface ValidLocalDateTime {
    String message() default "{javaday.istanbul.sliconf.micro." +
            "Invalid start and end date.}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
