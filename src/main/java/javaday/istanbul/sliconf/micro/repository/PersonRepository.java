package javaday.istanbul.sliconf.micro.repository;

import javaday.istanbul.sliconf.micro.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends CrudRepository<Person, String> {
    List<Person> findByFirstName(String firstName);
}
