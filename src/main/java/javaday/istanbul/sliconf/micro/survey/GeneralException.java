package javaday.istanbul.sliconf.micro.survey;

import java.util.Objects;

public class GeneralException extends RuntimeException {

    private final transient Object rejectedValue;

    public GeneralException(String message, Object rejectedValue) {
        super(message);
        this.rejectedValue = Objects.nonNull(rejectedValue) ? rejectedValue : "null";
    }

    @Override
    public final String getMessage() {
        return super.getMessage();
    }

    public Object getRejectedValue() {
        return this.rejectedValue;
    }

}
