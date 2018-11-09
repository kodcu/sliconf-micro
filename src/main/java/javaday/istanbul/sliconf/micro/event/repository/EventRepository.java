package javaday.istanbul.sliconf.micro.event.repository;

import javaday.istanbul.sliconf.micro.event.model.LifeCycleState;
import javaday.istanbul.sliconf.micro.event.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EventRepository extends MongoRepository<Event, String>,
        PagingAndSortingRepository<Event, String> {

    //@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.listEvents} AND Meta(events).id = $1")
    Optional<Event> findById(String id);

    Optional<Event> findByKey(String key);

    Page<Event> findByLifeCycleStateEventStatuses(List<LifeCycleState.EventStatus> eventStatuses, Pageable pageable);

    @Query("{'lifeCycleState.eventStatuses': ?#{$in:[0]} }")
    Page<Event> findByLifeCycleState_EventStatuses(List<LifeCycleState.EventStatus> eventStatuses, Pageable pageable);


    List<Event> findByName(String name);

    List<Event> findByNameAndDeleted(String name, Boolean deleted);

    Event findEventByKeyEquals(String key);

    List<Event> findAllByExecutiveUserEquals(String executiveUser);

    //@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.listEvents} AND executiveUser = $1 AND deleted = $2")
    List<Event> findAllByExecutiveUserEqualsAndDeleted(String executiveUser, Boolean deleted);

    List<Event> findAllByExecutiveUserAndDeleted(String executiveUser, Boolean deleted);

    Event findByKeyAndExecutiveUser(String key, String executiveUser);

    List<Event> findByNameAndKeyNot(String name, String key);

    List<Event> findByNameAndKeyNotAndDeleted(String name, String key, Boolean deleted);

}