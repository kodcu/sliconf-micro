package javaday.istanbul.sliconf.micro;

import com.devskiller.jfairy.Fairy;
import javaday.istanbul.sliconf.micro.agenda.AgendaGenerator;
import javaday.istanbul.sliconf.micro.event.EventGenerator;
import javaday.istanbul.sliconf.micro.event.EventSpecs;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.event.repository.EventRepository;
import javaday.istanbul.sliconf.micro.event.service.EventRepositoryService;
import javaday.istanbul.sliconf.micro.floor.FloorGenerator;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.room.RoomGenerator;
import javaday.istanbul.sliconf.micro.speaker.SpeakerGenerator;
import javaday.istanbul.sliconf.micro.sponsor.SponsorGenerator;
import javaday.istanbul.sliconf.micro.survey.AnswerRepository;
import javaday.istanbul.sliconf.micro.survey.SurveyRepository;
import javaday.istanbul.sliconf.micro.survey.model.Answer;
import javaday.istanbul.sliconf.micro.survey.model.Survey;
import javaday.istanbul.sliconf.micro.survey.service.AnswerService;
import javaday.istanbul.sliconf.micro.survey.service.SurveyService;
import javaday.istanbul.sliconf.micro.survey.util.AnswerGenerator;
import javaday.istanbul.sliconf.micro.survey.util.SurveyGenerator;
import javaday.istanbul.sliconf.micro.user.UserGenerator;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.user.repository.UserRepository;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Uygulama geliştirirken kullanılabilecek test verileri üretmede kullanılabilir.
 * Aktif profil dev yapılarak veritabanı için rastgele veri üretimi yapılabilir.
 * Daha sonra işlemin tekrar tekrar yapılmaması adına bu değişiklik geri alınmalıdır.
 * */
@Slf4j
@RequiredArgsConstructor
@Component
@Profile({"make it dev if you need test data"})
public class DataGenerator implements CommandLineRunner {

    private final EventRepositoryService eventRepositoryService;
    private final UserRepositoryService userRepositoryService;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final SurveyService surveyService;
    private final SurveyRepository surveyRepository;
    private final AnswerService answerService;
    private final AnswerRepository answerRepository;


    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        log.info("Data generation starts...");

        if (activeProfile.equals("prod"))
            SpringApplication.run(SliconfMicroApp.class, args).close();

        userRepository.deleteAll();
        eventRepository.deleteAll();
        surveyRepository.deleteAll();
        answerRepository.deleteAll();

        Fairy fairy = Fairy.create();
        for (int i = 0; i < 10; i++) {

            User user = UserGenerator.generateRandomUsers(1).stream().findFirst().orElse(null);
            assert user != null;

            userRepositoryService.saveUser(user);

            Set<Event> eventSet = EventGenerator.generateRandomEvents(1, user.getId());

            for (Event event : eventSet) {

                FloorGenerator.generateRandomFlors(1, event);
                RoomGenerator.generateRandomRooms(1, event);
                SpeakerGenerator.generateRandomSpeakers(15, event);
                AgendaGenerator.generateRandomAgendaElements(5, event);
                SponsorGenerator.generateRandomSponsors(10, event);
                EventSpecs.generateKanbanNumber(event, eventRepositoryService);
                eventRepositoryService.save(event);
            }

            eventSet.forEach(event -> {
                ResponseMessage responseMessage;
                List<Survey> surveys = SurveyGenerator.generateRandomSurveys(5, event, user.getId());

                Set<User> users = UserGenerator.generateRandomUsers(fairy.baseProducer().randomBetween(10, 35));

                users.forEach(userRepositoryService::saveUser);

                for (Survey survey : surveys) {

                    responseMessage = surveyService.addNewSurvey(survey, event.getId());
                    log.info(responseMessage.getMessage());

                    for (Answer answer : AnswerGenerator.generateRandomAnswersToSurvey(survey, users)) {
                        responseMessage = answerService.answerSurvey(answer, survey.getId(), event.getId());
                        log.info(responseMessage.getMessage());

                    }

                }
            });
        }
        log.info("Data generation has finished");
    }
}
