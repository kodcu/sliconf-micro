package javaday.istanbul.sliconf.micro.admin;

import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.event.model.EventFilter;
import javaday.istanbul.sliconf.micro.event.service.EventRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class AdminService {

    private final EventRepositoryService eventRepositoryService;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Transactional
    public Page<Event> filter(EventFilter eventFilter, Pageable pageable) {
        return eventRepositoryService.filter(eventFilter, pageable);

    }

}
