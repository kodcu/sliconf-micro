package javaday.istanbul.sliconf.micro.admin;

import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.event.model.LifeCycleState;
import javaday.istanbul.sliconf.micro.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class AdminMailService {

    private final EventRepository eventRepository;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Transactional
    public Set<Event> getEvents(){
        List<LifeCycleState.EventStatus> eventStatuses = new ArrayList<>();

        eventStatuses.add(LifeCycleState.EventStatus.ACTIVE);

        Set<Event> events = new HashSet<>();
        if(!activeProfile.equals("test"))
            for (LifeCycleState.EventStatus eventStatus : eventStatuses) {
                events.addAll(eventRepository.findAllByLifeCycleStateEventStatusesLike(eventStatus));
            }
            return events;

    }
}
