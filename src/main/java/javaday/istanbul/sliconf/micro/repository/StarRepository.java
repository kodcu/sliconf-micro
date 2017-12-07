package javaday.istanbul.sliconf.micro.repository;

import javaday.istanbul.sliconf.micro.model.event.agenda.Star;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StarRepository extends CrudRepository<Star, String> {

}