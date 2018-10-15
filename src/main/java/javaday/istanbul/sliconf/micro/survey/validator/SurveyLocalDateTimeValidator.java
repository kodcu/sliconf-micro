package javaday.istanbul.sliconf.micro.survey.validator;

import javaday.istanbul.sliconf.micro.survey.model.Survey;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Anketin başlangıç ve bitiş tarihini iş mantığına göre kontrol etmemizi sağlayan custom validator.
 */
@Component
public class SurveyLocalDateTimeValidator
        implements ConstraintValidator<ValidSurveyLocalDateTime, Survey> {

    @Override
    public void initialize(ValidSurveyLocalDateTime constraintAnnotation) {
        /**
         * Initializes the validator in preparation for
         * {@link #isValid(Object, ConstraintValidatorContext)} calls.
         * The constraint annotation for a given constraint declaration
         * is passed.
         * <p/>
         * This method is guaranteed to be called before any use of this instance for
         * validation.
         *
         * @param constraintAnnotation annotation instance for a given constraint declaration
         */
    }

    @Override
    public boolean isValid(Survey survey, ConstraintValidatorContext context) {
        if (survey == null) {
            return true;
        }

        long epochSecondStartTime = Long.parseLong(survey.getStartTime());
        LocalDateTime startDateTime = LocalDateTime.ofEpochSecond(epochSecondStartTime, 0, ZoneOffset.UTC);

        long epochSecondEndTime = Long.parseLong(survey.getEndTime());
        LocalDateTime endDateTime = LocalDateTime.ofEpochSecond(epochSecondEndTime, 0, ZoneOffset.UTC);

        if (startDateTime.isAfter(endDateTime)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{survey.startAndEndTime.invalid}")
                    .addConstraintViolation();
            return false;
        }

        if (startDateTime.isBefore(LocalDateTime.now().minusMinutes(1))) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{survey.startTime-after-now}")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}