package javaday.istanbul.sliconf.micro.survey;

import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Component
public class SurveyValidator {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    public ResponseMessage validate(List<Object> validatingObjects ) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setStatus(true);
        responseMessage.setMessage("");

        Set<ConstraintViolation<Object>> constraintViolations = new HashSet<>();
        validatingObjects.forEach(o -> constraintViolations.addAll(validator.validate(o)));

        responseMessage.setStatus(constraintViolations.isEmpty());
        List<Object> violatingObjects = new ArrayList<>();

        constraintViolations.forEach(constraintViolation -> {
            String constraintViolationMessages = responseMessage.getMessage();
            String rejectedValue = constraintViolation.getInvalidValue().toString();
            String newConstraintMessage = constraintViolation.getMessage() + " --> Invalid Value = " + rejectedValue + ", ";
            responseMessage.setMessage(constraintViolationMessages + newConstraintMessage);

            Predicate<Object> objectPredicate = o -> o.hashCode() == constraintViolation.getLeafBean().hashCode();

            /* kisitlamalari ihlal eden model kisitlamalari ihlal eden objeler listesinde degilse ekliyoruz. */
            violatingObjects.stream().filter(objectPredicate)
                    .findFirst().orElseGet(() -> violatingObjects.add(constraintViolation.getLeafBean()));
        });

        responseMessage.setReturnObject(violatingObjects);
        return responseMessage;
    }
}
