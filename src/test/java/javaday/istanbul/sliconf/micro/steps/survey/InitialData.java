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
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;


@Component
public class InitialData {

    @Autowired
    private EventRepositoryService eventRepositoryService;

    @Autowired
    private  UserRepositoryService userRepositoryService;

    @Autowired
    private SurveyService surveyService;

    protected User user;

    protected Event event;

    protected Survey survey;

    public   boolean checkErrorMessages(String exceptedErrorMessage, Survey invalidSurvey){
        ResponseMessage responseMessage;
        responseMessage = surveyService.addNewSurvey(invalidSurvey, this.event.getKey());
        String[] message =  responseMessage.getMessage().split("-->");
        return (exceptedErrorMessage.equals(message[0]));
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
                .setDate(LocalDateTime.now().plusMonths(1))
                .setKey("AAA1")
                .build();
//        ResponseMessage createEventMessage1 = createEventRoute.processEvent(event, userId1);
//        assertTrue(createEventMessage1.isStatus());
        eventRepositoryService.save(event);
        event.setId(eventRepositoryService.findByName(event.getName()).get(0).getId());
        survey = new Survey();
        survey.setEventId(event.getId());
        survey.setUserId(user.getId());
        survey.setName("Anket 01");
        survey.setEventKey(event.getKey());

        long date = LocalDateTime.now().plusDays(1).toEpochSecond(ZoneOffset.UTC);
        survey.setStartTime(Long.toString(date));
        long date2 = LocalDateTime.now().plusDays(2).toEpochSecond(ZoneOffset.UTC);
        survey.setEndTime(Long.toString(date2));

        Question question = new Question();
        question.setText("Soru 01");
        question.setId("questionId");
        List<Question> questions = new ArrayList<>();
        questions.add(question);

        QuestionOption questionOption1 = new QuestionOption();
        QuestionOption questionOption2 = new QuestionOption();
        questionOption1.setText("Seçenek 01"); questionOption1.setId("questionOption1Id");
        questionOption2.setText("Seçenek 02"); questionOption2.setId("questionOption2Id");
        List<QuestionOption> questionOptions = new ArrayList<>();
        questionOptions.add(questionOption1);
        questionOptions.add(questionOption2);
        question.setOptions(questionOptions);

        survey.setQuestions(questions);
        survey.setEventId(event.getId());
    }
}
