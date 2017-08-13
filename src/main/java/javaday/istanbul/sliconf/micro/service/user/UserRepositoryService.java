package javaday.istanbul.sliconf.micro.service.user;

import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Component
@Qualifier("UserRepositoryService")
public class UserRepositoryService implements UserService {

    @Autowired
    private UserRepository repo;

    public User findOne(String id) {
        return repo.findOne(id);
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<User>();

        for (User user : repo.findAll()) {
            users.add(user);
        }


        return users;
    }

    public List<User> findByName(String name) {
        return repo.findByName(name);
    }

    public void delete(User user) {
        repo.delete(user);
    }

    public ResponseMessage save(User user) {
        ResponseMessage message = new ResponseMessage(false, "An error occured while saving user", null);

        try {
            User savedUser = repo.save(user);

            if (Objects.nonNull(savedUser)) {
                message.setStatus(true);
                message.setMessage("User saved successfully!");

                savedUser.setHashedPassword("");
                savedUser.setSalt("");

                message.setReturnObject(savedUser);
            }

        } catch (Exception e) {
            // Todo loggger
            // logger.error(e.getMessage(), e);
        }

        return message;
    }
}