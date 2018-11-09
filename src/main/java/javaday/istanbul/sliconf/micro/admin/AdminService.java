package javaday.istanbul.sliconf.micro.admin;

import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.event.model.LifeCycleState;
import javaday.istanbul.sliconf.micro.event.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class AdminService {

    private final EventRepository eventRepository;

    @Transactional
    public Page<Event> listEvents(List<String> filters, Pageable pageable) {

        List<LifeCycleState.EventStatus>  lifeCycleStates = new ArrayList<>();

        for (String lifeCycleState : filters)
            lifeCycleStates.add(LifeCycleState.EventStatus.valueOf(lifeCycleState));

       return eventRepository.findByLifeCycleState_EventStatuses(lifeCycleStates, pageable);
    }
}
