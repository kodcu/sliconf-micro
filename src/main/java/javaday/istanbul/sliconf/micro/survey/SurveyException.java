package javaday.istanbul.sliconf.micro.survey;

public class SurveyException extends RuntimeException {

    private Object rejectedValue;
    public SurveyException(String message, Object rejectedValue) {
        super(message);
        this.rejectedValue = rejectedValue;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public Object getRejectedValue() {
        return this.rejectedValue;
    }

}
