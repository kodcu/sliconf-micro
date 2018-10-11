package javaday.istanbul.sliconf.micro.survey;

public class SurveyException extends RuntimeException {

    private final transient Object rejectedValue;
    public SurveyException(String message, Object rejectedValue) {
        super(message);
        this.rejectedValue = rejectedValue != null ? rejectedValue : "null";
    }
    @Override
    public final String getMessage() {
        return super.getMessage();
    }

    public Object getRejectedValue() {
        return this.rejectedValue;
    }

}
