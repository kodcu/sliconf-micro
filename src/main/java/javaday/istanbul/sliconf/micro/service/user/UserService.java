package javaday.istanbul.sliconf.micro.service.user;

import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;

import java.util.List;

public interface UserService {
    User findOne(String id);

    List<User> findAll();

    List<User> findByUsername(String username);

    void delete(User user);

    ResponseMessage save(User user);

    User findByEmail(String email);

    boolean controlIfEmailIsExists(String email);

    ResponseMessage changePassword(String email, String newPassword, String newPasswordAgain);

    User findById(String id);
}
