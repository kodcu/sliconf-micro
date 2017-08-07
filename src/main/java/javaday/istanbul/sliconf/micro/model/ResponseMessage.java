package javaday.istanbul.sliconf.micro.model;


import java.util.Objects;

/**
 * Created by ttayfur on 7/20/17.
 */
public class ResponseMessage {
    private boolean status;
    private String message;
    private Object returnObject;

    public ResponseMessage() {}

    public ResponseMessage(boolean status, String message, Object returnObject) {
        this.status = status;
        this.message = message;
        this.returnObject = returnObject;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }
}
