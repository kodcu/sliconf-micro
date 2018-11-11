package javaday.istanbul.sliconf.micro.user.service;

import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.survey.service.AnswerService;
import javaday.istanbul.sliconf.micro.survey.service.GeneralService;
import javaday.istanbul.sliconf.micro.user.UserRepositoryMessageProvider;
import javaday.istanbul.sliconf.micro.user.UserSpecs;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.user.repository.UserRepository;
import javaday.istanbul.sliconf.micro.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserRepositoryService implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserRepositoryService.class);

    @Autowired
    private GeneralService generalService;
    @Autowired
    private AnswerService answerService;

    @Autowired
    protected UserRepository repo;

    @Autowired
    private UserRepositoryMessageProvider userRepositoryMessageProvider;

    private String passDoNotMeetRequiredLength = "passwordDoNotMeetRequiredLength";

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        for (User user : repo.findAll()) {
            users.add(user);
        }

        return users;
    }

    /**
     * Anonim olmayan kullanicilari dondurur. Anoninm alaninin false ya da null olmasina bakar
     *
     * @param
     * @return
     */
    public List<User> findAllByAnonymous() {
        List<User> users = new ArrayList<>();

        for (User user : repo.findAll()) {
            if (Objects.nonNull(user) && Objects.nonNull(user.getAnonymous()) && !user.getAnonymous())
                users.add(user);
        }

        return users;
    }

    @Override
    public List<User> findByUsername(String username) {
        return repo.findByUsername(username);
    }

    @Override
    public List<User> findByDeviceId(String deviceId) {
        return repo.findByDeviceId(deviceId);
    }

    public List<User> findByUsernameDiffrentThenId(String username, String id) {
        List<User> users = repo.findByUsername(username);

        if (Objects.nonNull(users)) {
            users = users.stream().filter(user -> Objects.nonNull(user) &&
                    Objects.nonNull(user.getId()) && !user.getId().equals(id)
            ).collect(Collectors.toList());
        }

        if (Objects.nonNull(users) && users.isEmpty()) {
            return null; // NOSONAR
        } else {
            return users;
        }
    }

    public List<User> findUsersByEmail(String email) {
        return repo.findUsersByEmail(email);
    }

    @Override
    public void delete(User user) {
        repo.delete(user);
    }

    public void removeAllByEmailAndUsername(String email, String username) {
        repo.removeAllByEmailAndUsername(email, username);
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
                    String.format(userRepositoryMessageProvider.getMessage(passDoNotMeetRequiredLength),
                            Constants.MIN_PASS_LENGTH, Constants.MAX_PASS_LENGTH)
            );
            return responseMessage;
        }

        return responseMessage;
    }

    /**
     * Gonderilen password gerekli kriterleri karsiliyor mu kontrolu
     *
     * @param password
     * @return
     */
    private ResponseMessage isPasswordValid(String password) {
        ResponseMessage responseMessage = new ResponseMessage(true, "", "");

        if (!UserSpecs.isPassMeetRequiredLengths(password)) {
            responseMessage.setStatus(false);
            responseMessage.setMessage(
                    String.format(userRepositoryMessageProvider.getMessage(passDoNotMeetRequiredLength),
                            Constants.MIN_PASS_LENGTH, Constants.MAX_PASS_LENGTH)
            );
            return responseMessage;
        }

        return responseMessage;
    }

    @Override
    public Optional<User> findById(String id) {
        return repo.findById(id);
    }

    /**
     * Yeni kullanici ve kullanici update sirasinda kullanilan save metodu
     * Sifrenin kurallara uyup uymadigini kontrol ederek save islemi gerceklestirir
     *
     * @param user
     * @return
     */
    @Override
    public ResponseMessage saveUser(User user) {
        ResponseMessage responseMessage = new ResponseMessage(false,
                String.format(userRepositoryMessageProvider.getMessage(passDoNotMeetRequiredLength),
                        Constants.MIN_PASS_LENGTH, Constants.MAX_PASS_LENGTH), "");

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

    @Override
    public ResponseMessage saveAnonymousUser(User anonymousUser) {
        ResponseMessage responseMessage = new ResponseMessage(false,
                String.format(userRepositoryMessageProvider.getMessage(passDoNotMeetRequiredLength),
                        Constants.MIN_PASS_LENGTH, Constants.MAX_PASS_LENGTH), "");

        if (Objects.nonNull(anonymousUser)) {
            responseMessage = save(anonymousUser);
        }

        return responseMessage;
    }

    @Override
    // Kullanicinin bir eventteki anketlere verdigi cevaplari listeler
    public ResponseMessage getSurveyAnswers(String eventIdentifier, String userId) {

        Event event = (Event) generalService.findEventByIdOrEventKey(eventIdentifier).getReturnObject();
        generalService.findUserById(userId);

        return answerService.getEventSurveysAnswersOfUser(event.getId(), userId);

    }


}