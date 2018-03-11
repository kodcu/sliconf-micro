package javaday.istanbul.sliconf.micro.model.event;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Speaker {
    private String id;
    private String name;
    private String profilePicture;
    private String workingAt;
    private String about;
    private String twitter;
    private String linkedin;
    private List<String> topics;


}
