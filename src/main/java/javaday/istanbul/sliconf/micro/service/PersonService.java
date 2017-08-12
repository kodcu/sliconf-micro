package javaday.istanbul.sliconf.micro.service;

import javaday.istanbul.sliconf.micro.model.Person;

import java.util.List;

public interface PersonService {
    Person findOne(String id);

    List<Person> findAll();

    List<Person> findByFirstName(String firstName);

    void delete(Person person);

    void save(Person person);
}
