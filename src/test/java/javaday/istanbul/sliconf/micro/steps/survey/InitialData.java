package javaday.istanbul.sliconf.micro.steps.survey;

import javaday.istanbul.sliconf.micro.builder.EventBuilder;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.survey.model.Question;
import javaday.istanbul.sliconf.micro.survey.model.QuestionOption;
import javaday.istanbul.sliconf.micro.survey.model.Survey;
import javaday.istanbul.sliconf.micro.survey.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@PropertySource(value = {"classpath:ValidationMessages.properties", "classpath:survey.properties"})
@Component
public class InitialData { // NOSONAR

    protected User user;
    protected Event event;
    protected Survey survey;
    @Autowired
    Environment env;
    @Autowired
    EventRepositoryService eventRepositoryService;
    @Autowired
    UserRepositoryService userRepositoryService;
    @Autowired
    SurveyService surveyService;

    public InitialData() {
        user = new User();
        event = new Event();
        survey = Survey.builder().build();
    }

    private boolean checkErrorMessages(String exceptedErrorMessage, String returnedErrorMessage) {
        return (exceptedErrorMessage.equals(returnedErrorMessage));
    }

    boolean checkSurveyCreateErrorMessages(String exceptedErrorMessage, Survey invalidSurvey) {
        ResponseMessage responseMessage;
        responseMessage = surveyService.addNewSurvey(invalidSurvey, event.getKey());
        String[] message = responseMessage.getMessage().split(" -->");
        return checkErrorMessages(exceptedErrorMessage, message[0]);
    }

    boolean checkGetSurveyErrorMessages(String exceptedErrorMessage, Survey invalidSurvey) {
        ResponseMessage responseMessage;
        responseMessage = surveyService.getSurvey(invalidSurvey.getId());
        exceptedErrorMessage += " -->";
        String[] message = responseMessage.getMessage().split(" -->");
        return checkErrorMessages(exceptedErrorMessage, message[0]);
    }

    boolean checkAnswerCreateErrorMessages(String exceptedErrorMessage, String returnedErrorMessage) {
        String[] message = returnedErrorMessage.split(" -->");
        return checkErrorMessages(exceptedErrorMessage, message[0]);
    }

    void init() {
        user = new User();
        user.setUsername("createSurveyUser01");
        user.setEmail("createSurveyUser01@sliconf.com");
        user.setPassword("123123123");

        ResponseMessage savedUserMessage = userRepositoryService.saveUser(user);

        String userId1 = ((User) savedUserMessage.getReturnObject()).getId();

        event = new EventBuilder()
                .setName("Create Event 01")
                .setExecutiveUser(userId1)
                .setDate(LocalDateTime.now().plusWeeks(1))
                .setEndDate(LocalDateTime.now().plusWeeks(2))
                .setKey("AAA1")
                .build();
//        ResponseMessage createEventMessage1 = createEventRoute.processEvent(event, userId1);
//        assertTrue(createEventMessage1.isStatus());
        eventRepositoryService.save(event);
        event.setId(eventRepositoryService.findByName(event.getName()).get(0).getId());
        survey = Survey.builder()
                .eventId(event.getId())
                .userId(user.getId())
                .name("Anket 01")
                .eventKey(event.getKey())
                .participants(0)
                .build();

        long date = LocalDateTime.now().plusDays(1).toEpochSecond(ZoneOffset.UTC);
        survey.setStartTime(Long.toString(date));
        long date2 = LocalDateTime.now().plusDays(2).toEpochSecond(ZoneOffset.UTC);
        survey.setEndTime(Long.toString(date2));

        Question question = Question.builder()
                .text("Soru 01")
                .id("questionId")
                .totalVoters(0)
                .build();
        List<Question> questions = new ArrayList<>();
        questions.add(question);

        QuestionOption questionOption1 = QuestionOption.builder().voters(0).build();
        QuestionOption questionOption2 = QuestionOption.builder().voters(0).build();
        questionOption1.setText("Seçenek 01");
        questionOption1.setId("questionOption1Id");
        questionOption2.setText("Seçenek 02");
        questionOption2.setId("questionOption2Id");
        List<QuestionOption> questionOptions = new ArrayList<>();
        questionOptions.add(questionOption1);
        questionOptions.add(questionOption2);
        question.setOptions(questionOptions);

        survey.setQuestions(questions);
        survey.setEventId(event.getId());
    }
}
