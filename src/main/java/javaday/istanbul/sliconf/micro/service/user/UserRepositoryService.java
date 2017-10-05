package javaday.istanbul.sliconf.micro.service.user;

import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(UserRepositoryService.class);

    @Autowired
    private UserRepository repo;

    public User findOne(String id) {
        return repo.findOne(id);
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();

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

                savedUser.setHashedPassword(null);
                savedUser.setSalt(null);

                message.setReturnObject(savedUser);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return message;
    }

    /**
     * Verilen email ile bulunan kullanicilari dondurur.
     * Eger liste null ise bos liste dondurur
     *
     * @param email
     * @return
     */
    public List<User> findByEmail(String email) {
        List<User> users = repo.findByEmail(email);
        return Objects.nonNull(users) ? users : new ArrayList<>();
    }

    public User findFirstByEmailEquals(String email) {
        return repo.findFirstByEmail(email);
    }

    /**
     * Eger verilen email ile baska bir kullanici var ise true doner
     *
     * @param email
     * @return
     */
    public boolean controlIfEmailIsExists(String email) {
        return !findByEmail(email).isEmpty();
    }
}