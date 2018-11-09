package javaday.istanbul.sliconf.micro.event.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class LifeCycleState {

    private List<EventStatus> eventStatuses;

    public enum EventStatus {
        ACTIVE, PASSIVE, HAPPENING, FINISHED, DELETED, FAILED
    }
}
