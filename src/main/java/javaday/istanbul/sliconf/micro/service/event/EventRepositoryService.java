package javaday.istanbul.sliconf.micro.service.event;

import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
}