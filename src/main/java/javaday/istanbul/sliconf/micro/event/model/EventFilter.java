package javaday.istanbul.sliconf.micro.event.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EventFilter {

    private String name;

    private List<String> eventStatuses;
}
