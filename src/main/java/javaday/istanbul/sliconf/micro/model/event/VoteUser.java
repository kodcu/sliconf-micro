package javaday.istanbul.sliconf.micro.model.event;

import lombok.Getter;
import lombok.Setter;

/**
 * Oylarda gozuken user
 */
@Getter
@Setter
public class VoteUser {
    private String userId;
    private String username;
    private String fullname;
}
