package javaday.istanbul.sliconf.micro.repository;

import javaday.istanbul.sliconf.micro.model.event.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EventRepository extends CrudRepository<Event, String> {
    List<Event> findByName(String name);

    Event findEventByKeyEquals(String key);

    List<Event> findAllByExecutiveUserEquals(String executiveUser);

    Event findByKeyAndExecutiveUser(String key, String executiveUser);
}