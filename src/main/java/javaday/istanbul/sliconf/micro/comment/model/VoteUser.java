package javaday.istanbul.sliconf.micro.comment.model;

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
