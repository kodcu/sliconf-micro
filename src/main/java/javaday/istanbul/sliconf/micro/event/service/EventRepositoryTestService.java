package javaday.istanbul.sliconf.micro.event.service;

import javaday.istanbul.sliconf.micro.event.EventSpecs;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("test")
public class EventRepositoryTestService extends EventRepositoryService {

    @Override
    public List<Event> findByNameAndNotKeyAndDeleted(String name, String key, Boolean deleted) {
        List<Event> events = repo.findByNameAndDeleted(name, deleted);

        return getNotKeyEquals(events, key);
    }

    @Override
    public ResponseMessage save(Event event) {
        ResponseMessage message = new ResponseMessage(false, "An error occured while saving event", null);

        EventSpecs.generateStatusDetails(event);

        saveEvent(event, message);

        return message;
    }

}