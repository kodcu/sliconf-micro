package javaday.istanbul.sliconf.micro.speaker;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
