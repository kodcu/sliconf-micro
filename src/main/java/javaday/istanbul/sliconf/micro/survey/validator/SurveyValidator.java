package javaday.istanbul.sliconf.micro.survey.validator;

import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;
import java.util.function.Predicate;

@Component
public class SurveyValidator {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    public <T> ResponseMessage validate(List<Object> validatingObjects, Class<T> clazz) {
        ResponseMessage responseMessage = new ResponseMessage();

        responseMessage.setStatus(true);
        responseMessage.setMessage("");

        Set<ConstraintViolation<Object>> constraintViolations = new HashSet<>();
        validatingObjects.forEach(o -> constraintViolations.addAll(validator.validate(o, clazz)));

        responseMessage.setStatus(constraintViolations.isEmpty());
        List<Object> violatingObjects = new ArrayList<>();

        constraintViolations.forEach(constraintViolation -> {
            String constraintViolationMessages = responseMessage.getMessage();

            String rejectedValue;

            if (Objects.isNull(constraintViolation.getInvalidValue()))
                rejectedValue = "null";
            else
                rejectedValue = constraintViolation.getInvalidValue().toString();

            String newConstraintMessage = constraintViolation.getMessage() + " --> Invalid Value = " + rejectedValue + ", ";
            responseMessage.setMessage(constraintViolationMessages + newConstraintMessage);

            Predicate<Object> objectPredicate = o -> o.hashCode() == constraintViolation.getLeafBean().hashCode();

            /* kisitlamalari ihlal eden model kisitlamalari ihlal eden objeler listesinde degilse ekliyoruz. */
            if(violatingObjects.stream().noneMatch(objectPredicate))
                violatingObjects.add(constraintViolation.getLeafBean());
        });

        responseMessage.setReturnObject(violatingObjects);
        return responseMessage;
    }
}
