package javaday.istanbul.sliconf.micro.service.event;

import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;

import java.util.List;

public interface EventService {
    Event findOne(String id);

    List<Event> findAll();

    List<Event> findByName(String name);

    void delete(Event event);

    ResponseMessage save(Event event);
}
