package javaday.istanbul.sliconf.micro.event.repository;

import javaday.istanbul.sliconf.micro.event.model.BaseEventState;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EventStateRepository extends MongoRepository<BaseEventState, String>,
        CrudRepository<BaseEventState, String> {

    BaseEventState findById(String id);

    BaseEventState findByName(String name);

    BaseEventState findByType(String type);
}