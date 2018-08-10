package javaday.istanbul.sliconf.micro.survey;

import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Component
@AllArgsConstructor
public class SurveyValidator {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();


    ResponseMessage validate(Survey survey) {
        ResponseMessage responseMessage = new ResponseMessage();


        responseMessage.setStatus(true);

        Set<ConstraintViolation<Survey>> constraintViolations = validator.validate(survey);
        constraintViolations.stream().findAny().ifPresent(constraintViolation -> {
            responseMessage.setMessage(constraintViolation.getMessage());
            responseMessage.setStatus(false);
            responseMessage.setReturnObject(survey);
                });

        return responseMessage;
    }
}
