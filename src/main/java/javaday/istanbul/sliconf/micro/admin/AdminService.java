package javaday.istanbul.sliconf.micro.admin;

import com.google.api.client.util.Lists;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.event.model.LifeCycleState;
import javaday.istanbul.sliconf.micro.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class AdminService {

    private final EventRepository eventRepository;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Transactional
    public Page<Event> listEvents(List<String> filters, Pageable pageable) {

        List<LifeCycleState.EventStatus> eventStatuses;
        if (filters.isEmpty()) {
            filters.add("ACTIVE");
        }
        eventStatuses = filters.stream().map(LifeCycleState.EventStatus::valueOf).collect(Collectors.toList());

        // Query sorgulari test veritabani uzerinde calismiyor. o yuzden sadece prod ve devde calistiriyoruz.
        if(!activeProfile.equals("test"))
            return eventRepository.findAllByLifeCycleStateEventStatuses(eventStatuses, pageable);

        Set<Event> events = new HashSet<>();
        eventStatuses.stream().map(eventRepository::findAllByLifeCycleStateEventStatusesLike).forEach(events::addAll);

        return new PageImpl<>(Lists.newArrayList(events), pageable, events.size());
    }
}
