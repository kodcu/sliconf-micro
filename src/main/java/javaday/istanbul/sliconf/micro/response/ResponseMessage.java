package javaday.istanbul.sliconf.micro.response;


import lombok.Getter;
import lombok.Setter;

/**
 * Created by ttayfur on 7/20/17.
 */
@Getter
@Setter
public class ResponseMessage {
    private boolean status;
    private String message;
    private Object returnObject;

    public ResponseMessage() {
    }

    public ResponseMessage(boolean status, String message, Object returnObject) {
        this.status = status;
        this.message = message;
        this.returnObject = returnObject;
    }
}
