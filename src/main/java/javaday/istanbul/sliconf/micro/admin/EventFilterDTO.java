package javaday.istanbul.sliconf.micro.admin;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EventFilterDTO {

    private String onlyUpcomingEvents;

    private String onlyActiveEvents;


}
