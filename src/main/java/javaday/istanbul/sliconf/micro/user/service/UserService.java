package javaday.istanbul.sliconf.micro.user.service;

import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    List<User> findByUsername(String username);

    List<User> findByDeviceId(String deviceId);

    void delete(User user);

    ResponseMessage save(User user);

    User findByEmail(String email);

    boolean controlIfEmailIsExists(String email);

    ResponseMessage changePassword(String email, String newPassword, String newPasswordAgain);

    Optional<User> findById(String id);

    ResponseMessage saveUser(User saltedUser);

    ResponseMessage saveAnonymousUser(User anonymousUser);

    ResponseMessage getSurveyAnswers(String eventIdentifier, String userId);
}
