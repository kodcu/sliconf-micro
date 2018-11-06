package javaday.istanbul.sliconf.micro.model.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Location {

    private String lat;

    private String lng;

    private String description;
    
    private String venue;

}
