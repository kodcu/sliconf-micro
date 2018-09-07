package javaday.istanbul.sliconf.micro.survey.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.DateTimeException;
import java.time.LocalDateTime;

public class LocalDateTimeValidator
        implements ConstraintValidator<ValidLocalDateTime, LocalDateTime> {
    @Override
    public void initialize(ValidLocalDateTime constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDateTime localDateTime, ConstraintValidatorContext context) {
        if(localDateTime == null)
            return false;
        try {
            LocalDateTime.of(localDateTime.toLocalDate(), localDateTime.toLocalTime());
        } catch (DateTimeException e) {
            return false;
        }
        return true;
    }
}
