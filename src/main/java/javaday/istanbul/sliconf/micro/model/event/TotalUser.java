package javaday.istanbul.sliconf.micro.model.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalUser {

    private String id;
    private String username;
    private String email;
    private String fullname;
    private Boolean anonymous;
    private String deviceId;
}
