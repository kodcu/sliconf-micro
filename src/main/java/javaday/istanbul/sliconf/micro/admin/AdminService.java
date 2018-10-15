package javaday.istanbul.sliconf.micro.admin;

import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.event.LifeCycleState;
import javaday.istanbul.sliconf.micro.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
class AdminService {

    private final EventRepository eventRepository;

    @Transactional
    public Page<Event> filter(String[] lifeCycleStatesQuery, Pageable pageable) {

        List<LifeCycleState.EventStatus>  lifeCycleStates = new ArrayList<>();

        for (String lifeCycleState : lifeCycleStatesQuery)
            lifeCycleStates.add(LifeCycleState.EventStatus.valueOf(lifeCycleState));

       return eventRepository.findByLifeCycleStateEventStatuses(lifeCycleStates, pageable);
    }
}
