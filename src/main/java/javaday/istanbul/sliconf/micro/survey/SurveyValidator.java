package javaday.istanbul.sliconf.micro.survey;

import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Component
public class SurveyValidator {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    ResponseMessage validate(Object validatingObject) {
        ResponseMessage responseMessage = new ResponseMessage();

        responseMessage.setStatus(true);
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(validatingObject);

        constraintViolations.stream().findAny().ifPresent(constraintViolation -> {
            responseMessage.setMessage(constraintViolation.getMessage());
            responseMessage.setStatus(false);
            responseMessage.setReturnObject(validatingObject);
        });

        return responseMessage;
    }
}
