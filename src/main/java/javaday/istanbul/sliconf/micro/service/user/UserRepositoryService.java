package javaday.istanbul.sliconf.micro.service.user;

import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.UserRepositoryMessageProvider;
import javaday.istanbul.sliconf.micro.repository.UserRepository;
import javaday.istanbul.sliconf.micro.service.UserPassService;
import javaday.istanbul.sliconf.micro.specs.UserSpecs;
import javaday.istanbul.sliconf.micro.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserRepositoryService implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserRepositoryService.class);

    @Autowired
    private UserRepository repo;

    @Autowired
    private UserRepositoryMessageProvider userRepositoryMessageProvider;

    @Override
    public User findOne(String id) {
        return repo.findOne(id);
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        for (User user : repo.findAll()) {
            users.add(user);
        }

        return users;
    }

    @Override
    public List<User> findByUsername(String username) {
        return repo.findByUsername(username);
    }

    public List<User> findByUsernameDiffrentThenId(String username, String id) {
        return repo.findByUsernameAndIdNot(username, id);
    }

    @Override
    public void delete(User user) {
        repo.delete(user);
    }

    @Override
    public ResponseMessage save(User user) {
        ResponseMessage message = new ResponseMessage(false, "An error occured while saving user", null);

        try {

            User savedUser = repo.save(user);

            if (Objects.nonNull(savedUser)) {

                savedUser = new User(savedUser);

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
     * Verilen email ile iliskili kullaniciyi dondurur
     *
     * @param email
     * @return
     */
    @Override
    public User findByEmail(String email) {
        return repo.findUserByEmailEquals(email);
    }


    /**
     * Eger verilen email ile baska bir kullanici var ise true doner
     *
     * @param email
     * @return
     */
    @Override
    public boolean controlIfEmailIsExists(String email) {
        return Objects.nonNull(findByEmail(email));
    }

    /**
     * gonderilen sifreyi email ile iliskili kullanicinin uzerine yazar
     *
     * @param email
     * @param newPassword
     * @param newPasswordAgain
     * @return
     */
    @Override
    public ResponseMessage changePassword(String email, String newPassword, String newPasswordAgain) {
        ResponseMessage responseMessage = new ResponseMessage(false,
                userRepositoryMessageProvider.getMessage("passwordsDoNotMatch"), "");

        ResponseMessage isValidResponse = this.isNewPasswordValid(newPassword, newPasswordAgain);

        if (isValidResponse.isStatus()) {
            User dbUser = this.findByEmail(email);

            if (Objects.nonNull(dbUser)) {
                dbUser.setPassword(newPassword);

                UserPassService userPassService = new UserPassService();

                User newUser = userPassService.createNewUserWithHashedPassword(dbUser);

                ResponseMessage saveResponseMessage = this.save(newUser);

                if (Objects.nonNull(saveResponseMessage) && saveResponseMessage.isStatus()) {
                    responseMessage.setStatus(true);
                    responseMessage.setMessage(userRepositoryMessageProvider.getMessage("passwordSuccessullyChanged"));
                } else {
                    responseMessage.setMessage(userRepositoryMessageProvider.getMessage("updatedUserCouldNotSave"));
                }
            }
        } else {
            return isValidResponse;
        }

        return responseMessage;
    }

    /**
     * Gonderilen passwords gerekli kriterleri karsiliyor mu kontrolu
     *
     * @param password
     * @param passwordAgain
     * @return
     */
    private ResponseMessage isNewPasswordValid(String password, String passwordAgain) {
        ResponseMessage responseMessage = new ResponseMessage(true, "", "");

        if (!UserSpecs.isPasswordsEquals(password, passwordAgain)) {
            responseMessage.setStatus(false);
            responseMessage.setMessage(userRepositoryMessageProvider.getMessage("passwordsDoNotMatch"));
            return responseMessage;
        } else if (!UserSpecs.isPassMeetRequiredLengths(password)) {
            responseMessage.setStatus(false);
            responseMessage.setMessage(
                    String.format(userRepositoryMessageProvider.getMessage("passwordDoNotMeetRequiredLength"),
                            Constants.MIN_PASS_LENGTH, Constants.MAX_PASS_LENGTH)
                    );
            return responseMessage;
        }

        return responseMessage;
    }

    /**
     * Gonderilen password gerekli kriterleri karsiliyor mu kontrolu
     * @param password
     * @return
     */
    private ResponseMessage isPasswordValid(String password) {
        ResponseMessage responseMessage = new ResponseMessage(true, "", "");

        if (!UserSpecs.isPassMeetRequiredLengths(password)) {
            responseMessage.setStatus(false);
            responseMessage.setMessage(
                    String.format(userRepositoryMessageProvider.getMessage("passwordDoNotMeetRequiredLength"),
                            Constants.MIN_PASS_LENGTH, Constants.MAX_PASS_LENGTH)
            );
            return responseMessage;
        }

        return responseMessage;
    }

    @Override
    public User findById(String id) {
        return repo.findOne(id);
    }

    @Override
    public ResponseMessage saveUser(User user) {
        ResponseMessage responseMessage = new ResponseMessage(false,
                String.format(userRepositoryMessageProvider.getMessage("passwordDoNotMeetRequiredLength"),
                        Constants.MIN_PASS_LENGTH, Constants.MAX_PASS_LENGTH)
        , "");

        if (Objects.nonNull(user)) {
            responseMessage = isPasswordValid(user.getPassword());

            if (!responseMessage.isStatus()) {
                return responseMessage;
            }

            UserPassService userPassService = new UserPassService();
            User saltedUser = userPassService.createNewUserWithHashedPassword(user);

            responseMessage = save(saltedUser);
        }

        return responseMessage;
    }

}