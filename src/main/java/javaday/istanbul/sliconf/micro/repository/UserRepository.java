package javaday.istanbul.sliconf.micro.repository;

import javaday.istanbul.sliconf.micro.model.User;
import org.springframework.data.couchbase.core.query.View;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends CrudRepository<User, String> {

    /**
     * Return all users emitted by the view userInfo/adults
     */
    @View
    List<User> findAdults();

    /**
     * Find all users matching the last name.
     */
    @View(viewName = "lastNames")
    List<User> findByLastname(String lastName);

    /**
     * Find all the users whose first name contains the word.
     */
    List<User> findByFirstnameContains(String word);

}