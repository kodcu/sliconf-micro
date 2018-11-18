package javaday.istanbul.sliconf.micro.event.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
/**
 * 
 */
// TODO: 13.11.2018 aciklama yaz. 
public class LifeCycleState implements Serializable {

    private List<LifeCycleState.EventStatus> eventStatuses = new ArrayList<>();
    
    public enum EventStatus {
        ACTIVE, PASSIVE, HAPPENING, FINISHED, DELETED, FAILED
    }
}
