package javaday.istanbul.sliconf.micro.repository;

import javaday.istanbul.sliconf.micro.model.event.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EventRepository extends MongoRepository<Event, String>,
        CrudRepository<Event, String> {

    //@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND Meta(events).id = $1")
    Optional<Event> findById(String id);
    Optional<Event> findByKey(String key);

    List<Event> findByName(String name);

    List<Event> findByNameAndDeleted(String name, Boolean deleted);

    Event findEventByKeyEquals(String key);

    List<Event> findAllByExecutiveUserEquals(String executiveUser);

    //@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND executiveUser = $1 AND deleted = $2")
    List<Event> findAllByExecutiveUserEqualsAndDeleted(String executiveUser, Boolean deleted);

    List<Event> findAllByExecutiveUserAndDeleted(String executiveUser, Boolean deleted);

    Event findByKeyAndExecutiveUser(String key, String executiveUser);

    List<Event> findByNameAndKeyNot(String name, String key);

    List<Event> findByNameAndKeyNotAndDeleted(String name, String key, Boolean deleted);

    Optional<Event> findByIdOrKey(String identifier);

}