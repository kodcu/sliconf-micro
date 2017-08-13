package javaday.istanbul.sliconf.micro.service.user;

import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.model.User;

import java.util.List;

public interface UserService {
    User findOne(String id);

    List<User> findAll();

    List<User> findByName(String name);

    void delete(User user);

    ResponseMessage save(User user);
}
