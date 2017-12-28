package javaday.istanbul.sliconf.micro.repository;

import javaday.istanbul.sliconf.micro.model.event.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EventRepository extends CrudRepository<Event, String> {
    List<Event> findByName(String name);

    List<Event> findByNameAndDeleted(String name, Boolean deleted);

    Event findEventByKeyEquals(String key);

    List<Event> findAllByExecutiveUserEquals(String executiveUser);

    List<Event> findAllByExecutiveUserAndDeleted(String executiveUser, Boolean deleted);

    Event findByKeyAndExecutiveUser(String key, String executiveUser);

    List<Event> findByNameAndKeyNot(String name, String key);

    List<Event> findByNameAndKeyNotAndDeleted(String name, String key, Boolean deleted);
}