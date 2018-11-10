package javaday.istanbul.sliconf.micro.event.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class LifeCycleState implements Serializable {

    private List<LifeCycleState.EventStatus> eventStatuses;

    public enum EventStatus {
        ACTIVE, PASSIVE, HAPPENING, FINISHED, DELETED, FAILED
    }
}
