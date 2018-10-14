package javaday.istanbul.sliconf.micro.admin;

import lombok.AllArgsConstructor;
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
        ACTIVE, PASSIVE, UPCOMING, HAPPENING, FINISHED, DELETED, FAILED
    }
}
