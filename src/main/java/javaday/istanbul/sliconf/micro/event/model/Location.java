package javaday.istanbul.sliconf.micro.event.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Location {

    private String lat;

    private String lng;

    private String description;

    private String venue;


}
