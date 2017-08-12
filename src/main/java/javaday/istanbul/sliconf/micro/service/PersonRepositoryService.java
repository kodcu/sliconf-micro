package javaday.istanbul.sliconf.micro.service;

import javaday.istanbul.sliconf.micro.model.Person;
import javaday.istanbul.sliconf.micro.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Component
@Qualifier("PersonRepositoryService")
public class PersonRepositoryService implements PersonService {

    @Autowired
    private PersonRepository repo;

    public Person findOne(String id) {
        return repo.findOne(id);
    }

    public List<Person> findAll() {
        List<Person> people = new ArrayList<Person>();
        Iterator<Person> it = repo.findAll().iterator();
        while (it.hasNext()) {
            people.add(it.next());
        }


        return people;
    }

    public List<Person> findByFirstName(String firstName) {
        return repo.findByFirstName(firstName);
    }

    public void delete(Person person) {
        repo.delete(person);
    }

    public void save(Person person) {
        repo.save(person);
    }
}