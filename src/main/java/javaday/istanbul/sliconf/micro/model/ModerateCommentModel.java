package javaday.istanbul.sliconf.micro.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ModerateCommentModel {
    private String eventId;
    private String userId;
    private List<String> approved;
    private List<String> denied;

}
