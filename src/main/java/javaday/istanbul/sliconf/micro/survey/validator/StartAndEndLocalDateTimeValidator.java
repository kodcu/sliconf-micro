package javaday.istanbul.sliconf.micro.survey.validator;

import javaday.istanbul.sliconf.micro.survey.model.Survey;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class StartAndEndLocalDateTimeValidator
        implements ConstraintValidator<ValidStartAndEndLocalDateTime, Survey> {
    @Override
    public void initialize(ValidStartAndEndLocalDateTime constraintAnnotation) {

    }

    @Override
    public boolean isValid(Survey survey, ConstraintValidatorContext context) {
        if ( survey == null ) {
            return true;
        }

        LocalDateTime startDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(survey.getStartTime())), ZoneId.systemDefault());
        LocalDateTime endDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(survey.getEndTime())), ZoneId.systemDefault());


        if (startDateTime.isAfter(endDateTime)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{survey.startAndEndTime.invalid}")
                    .addConstraintViolation();
            return false;
        }

        if (startDateTime.isBefore(LocalDateTime.now().minusMinutes(1))) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{survey.startTime.invalid}")
            .addConstraintViolation();
            return false;
        }
        return true;
    }
}
