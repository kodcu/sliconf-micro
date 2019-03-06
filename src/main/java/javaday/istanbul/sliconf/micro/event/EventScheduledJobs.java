package javaday.istanbul.sliconf.micro.event;

import javaday.istanbul.sliconf.micro.admin.AdminService;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.event.model.EventFilter;
import javaday.istanbul.sliconf.micro.event.model.LifeCycleState;
import javaday.istanbul.sliconf.micro.event.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
@Profile({"prod", "dev"})
public class EventScheduledJobs {

    private final AdminService adminService;
    private final EventRepository eventRepository;

    /**
     *  Her yarim saatte bir aktif ,pasif ve gerceklesmekte olan eventlerin durumunu gunceller.
     * {@link LifeCycleState}
     * {@link LifeCycleState.EventStatus}
     */
    @Scheduled(fixedRate = 1800000, initialDelay = 1000 )
    private void updateEventLifeCycles() {
        // TODO Scheduled mantıgı kontrol

        List<String> filters = new ArrayList<>();
        filters.add("ACTIVE");
        filters.add("PASSIVE");
        filters.add("HAPPENING");
        Pageable pageable = new PageRequest(0,10000);
        EventFilter eventFilter = EventFilter.builder().eventStatuses(filters).nameLike("").build();
        Page<Event> events = adminService.filter(eventFilter, pageable);
        events.forEach(event -> {
           boolean isActive = event.getLifeCycleState().getEventStatuses().contains(LifeCycleState.EventStatus.ACTIVE);
           boolean isPassive = event.getLifeCycleState().getEventStatuses().contains(LifeCycleState.EventStatus.PASSIVE);
           boolean isHappening = event.getLifeCycleState().getEventStatuses().contains(LifeCycleState.EventStatus.HAPPENING);

           if(isActive && event.getStartDate().isBefore(LocalDateTime.now())) {
               event.getLifeCycleState().getEventStatuses().remove(LifeCycleState.EventStatus.ACTIVE);
               event.getLifeCycleState().getEventStatuses().add(LifeCycleState.EventStatus.HAPPENING);
               log.info("event status updated from active to happening");
           }

           if(isHappening && event.getEndDate().isBefore(LocalDateTime.now())) {
               event.getLifeCycleState().getEventStatuses().remove(LifeCycleState.EventStatus.HAPPENING);
               event.getLifeCycleState().getEventStatuses().add(LifeCycleState.EventStatus.FINISHED);
               log.info("event status updated from happening to finished");
           }

           if(isPassive && event.getStartDate().isBefore(LocalDateTime.now())) {
               event.getLifeCycleState().getEventStatuses().remove(LifeCycleState.EventStatus.PASSIVE);
               event.getLifeCycleState().getEventStatuses().add(LifeCycleState.EventStatus.FAILED);
               log.info("event status updated from passive to failed");

           }

           eventRepository.save(event);
        });

    }

}
