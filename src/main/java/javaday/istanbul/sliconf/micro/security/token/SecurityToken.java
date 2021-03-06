package javaday.istanbul.sliconf.micro.security.token;

import javaday.istanbul.sliconf.micro.user.model.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Hazelcast uzerinde tutulan authetication tokeni
 */
@Getter
@Setter
public class SecurityToken implements Serializable {

    private String username;
    private String role;
    private User user;
    private Date validUntilDate;

}
