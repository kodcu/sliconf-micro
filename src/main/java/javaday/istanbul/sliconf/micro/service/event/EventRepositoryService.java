package javaday.istanbul.sliconf.micro.service.event;

import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.repository.EventRepository;
import javaday.istanbul.sliconf.micro.specs.EventSpecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Component
@Qualifier("EventRepositoryService")
public class EventRepositoryService implements EventService {

    private Logger logger = LoggerFactory.getLogger(EventRepositoryService.class);

    @Autowired
    private EventRepository repo;

    public Event findOne(String id) {
        return repo.findOne(id);
    }

    public List<Event> findAll() {
        List<Event> events = new ArrayList<>();

        for (Event event : repo.findAll()) {
            events.add(event);
        }


        return events;
    }

    public List<Event> findByName(String name) {
        return repo.findByName(name);
    }

    public void delete(Event event) {
        repo.delete(event);
    }

    public ResponseMessage save(Event event) {
        ResponseMessage message = new ResponseMessage(false, "An error occured while saving event", null);

        try {
            Event savedEvent = repo.save(event);

            if (Objects.nonNull(savedEvent)) {
                message.setStatus(true);
                message.setMessage("Event saved successfully!");
                message.setReturnObject(savedEvent);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return message;
    }

    @Override
    public Event findEventByKeyEquals(String key) {
        return repo.findEventByKeyEquals(key);
    }

    @Override
    public Map<String, Event> findByExecutiveUser(String executiveUser) {
        Map<String, Event> events = new HashMap<>();

        final List<Event> eventList = repo.findAllByExecutiveUserEquals(executiveUser);

        final LocalDateTime now = LocalDateTime.now();

        if (Objects.nonNull(eventList)) {
            eventList.forEach(event -> {
                if (Objects.nonNull(event) && Objects.nonNull(event.getDate())) {
                    if (EventSpecs.checkIfEventDateAfterFromGivenDate(event, now)) {
                        events.put("active", event);
                    } else {
                        events.put("passive", event);
                    }
                }
            });
        }

        return events;
    }


}