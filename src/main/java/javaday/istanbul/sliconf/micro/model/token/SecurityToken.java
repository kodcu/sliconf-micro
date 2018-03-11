package javaday.istanbul.sliconf.micro.model.token;

import javaday.istanbul.sliconf.micro.model.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class SecurityToken implements Serializable {

    private String username;
    private String role;
    private User user;
    private Date validUntilDate;

}
