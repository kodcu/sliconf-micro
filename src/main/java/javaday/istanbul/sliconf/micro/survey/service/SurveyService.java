package javaday.istanbul.sliconf.micro.survey.service;

import javaday.istanbul.sliconf.micro.config.SpringConfigurations;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.event.agenda.AgendaElement;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.CommentMessageProvider;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.survey.SurveyException;
import javaday.istanbul.sliconf.micro.survey.SurveyMessageProvider;
import javaday.istanbul.sliconf.micro.survey.SurveyRepository;
import javaday.istanbul.sliconf.micro.survey.SurveyValidator;
import javaday.istanbul.sliconf.micro.survey.model.Answer;
import javaday.istanbul.sliconf.micro.survey.model.Question;
import javaday.istanbul.sliconf.micro.survey.model.Survey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Slf4j
@Service

public class SurveyService {

    private final Environment environment;
    private final GeneralService generalService;
    private final SurveyValidator surveyValidator;
    private final SurveyRepository surveyRepository;
    private final  SurveyMessageProvider surveyMessageProvider;


    public ResponseMessage addNewSurvey(Survey survey) throws RuntimeException {

        List<Object> validatingObjects = new ArrayList<>();
        // validate edilecek objeleri ekle.
        validatingObjects.add(survey);
        survey.getQuestions().forEach(validatingObjects::add);

        survey.getQuestions().forEach(question -> question.getOptions().forEach(validatingObjects::add));
        ResponseMessage responseMessage;
        responseMessage = surveyValidator.validate(validatingObjects);
        if (!responseMessage.isStatus()) {
            return responseMessage;
        }


        ResponseMessage eventResponse= generalService.findEventByKey(survey.getEventKey());
        Event event = (Event)  eventResponse.getReturnObject();

        ResponseMessage userResponse = generalService.findUserById(survey.getUserId());
        User user = (User) userResponse.getReturnObject();


        // TODO: 10.08.2018 Validate questions somehow before save.
        survey.setEventKey(event.getKey());
        survey.setUserId(user.getId());
        survey.setTime(LocalDateTime.now());

        // mongodb embedded elemanlar icin id olusturmaz. biz olusturuyoruz. sadece app-prodda calisir.
        if(Arrays.stream(environment.getActiveProfiles()).anyMatch(s -> s.contains("app-prod")))
            survey.getQuestions().stream().forEach(question -> {
                question.setId(new ObjectId().toString());
                question.setTotalVoters(0);
                question.getOptions().stream()
                        .forEach(questionOption -> {
                            questionOption.setId(new ObjectId().toString());
                            questionOption.setVoters(0);

                        });
            });

        survey.setParticipants(0);
        survey.setViewers(0);
        survey.getQuestions().forEach(question -> question.setTotalVoters(0));
        surveyRepository.save(survey);

        String message = surveyMessageProvider.getMessage("surveyCreatedSuccessfully");
        return new ResponseMessage(true, message, survey);

    }

    public ResponseMessage deleteSurvey(String userId, String surveyId) {
        ResponseMessage responseMessage = new ResponseMessage();

        generalService.findUserById(userId);
        ResponseMessage surveyResponseMessage =  generalService.findSurveyById(surveyId);
        Survey survey = (Survey) surveyResponseMessage.getReturnObject();

        responseMessage.setReturnObject(survey);
        responseMessage.setStatus(true);
        responseMessage.setMessage(surveyMessageProvider.getMessage("surveyDeletedSuccessfully"));

        surveyRepository.delete(surveyId);
        return responseMessage;
    }

    public ResponseMessage updateSurvey(Survey survey) {
        ResponseMessage responseMessage;
        List<Object> validatingObjects = new ArrayList<>();
        /* validate edilecek objeleri ekle. */
        validatingObjects.add(survey);
        survey.getQuestions().forEach(validatingObjects::add);

        survey.getQuestions().forEach(question -> question.getOptions().forEach(validatingObjects::add));
        responseMessage = surveyValidator.validate(validatingObjects);
        if (!responseMessage.isStatus()) {
            return responseMessage;
        }

        generalService.findSurveyById(survey.getId());
        generalService.findUserById(survey.getUserId());
        generalService.findEventByKey(survey.getEventKey());

        surveyRepository.save(survey);
        String message = surveyMessageProvider.getMessage("surveyUpdatedSuccessfully");
        return new ResponseMessage(true, message, survey);

    }

    public ResponseMessage getSurveys(String eventKey) {
        ResponseMessage responseMessage = new ResponseMessage();
        //check if event exists.
        generalService.findEventByKey(eventKey);

        List<Survey> surveys = surveyRepository.findSurveysByEventKey(eventKey);
        responseMessage.setStatus(true);
        responseMessage.setMessage(surveyMessageProvider.getMessage("surveysListedSuccessfully"));
        responseMessage.setReturnObject(surveys);
        return responseMessage;
    }

    public ResponseMessage getSurvey(String surveyId) {
        return generalService.findSurveyById(surveyId);
    }




}
