package javaday.istanbul.sliconf.micro.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by ttayfur on 7/6/17.
 */
@Getter
@Setter
public class ResponseError {

    private String message;

    public ResponseError(String message, String... args) {
        this.message = String.format(message, args);
    }

    public ResponseError(Exception e) {
        this.message = e.getMessage();
    }
}