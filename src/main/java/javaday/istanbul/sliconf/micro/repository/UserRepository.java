package javaday.istanbul.sliconf.micro.repository;

import javaday.istanbul.sliconf.micro.model.User;
import org.springframework.data.repository.CrudRepository;


//@Repository
public interface UserRepository extends CrudRepository<User, String> {

}