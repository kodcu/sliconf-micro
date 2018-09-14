package javaday.istanbul.sliconf.micro.survey.service;

import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.survey.SurveyMessageProvider;
import javaday.istanbul.sliconf.micro.survey.SurveyRepository;
import javaday.istanbul.sliconf.micro.survey.validator.SurveyValidator;
import javaday.istanbul.sliconf.micro.survey.model.Answer;
import javaday.istanbul.sliconf.micro.survey.model.Survey;
import javaday.istanbul.sliconf.micro.survey.validator.SurveyValidatorSequence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class SurveyService {

    private final Environment environment;
    private final GeneralService generalService;
    private final SurveyValidator surveyValidator;
    private final SurveyRepository surveyRepository;
    private final SurveyMessageProvider surveyMessageProvider;

    /* Answer service surveyservice'e surveyservice ise answerService' bağımlı.
       bu sebeple bunları @Autowired ile lazy load ediyoruz. böylece circular dependency hatası almıyoruz.
    */
    @Autowired
    private  AnswerService answerService;

    @Transactional
    public ResponseMessage addNewSurvey(Survey survey, String eventIdentifier) {

        List<Object> validatingObjects = new ArrayList<>();
        // validate edilecek objeleri ekle.
        validatingObjects.add(survey);
        survey.getQuestions().forEach(validatingObjects::add);

        survey.getQuestions().forEach(question -> question.getOptions().forEach(validatingObjects::add));
        ResponseMessage responseMessage;
        responseMessage = surveyValidator.validate(validatingObjects, SurveyValidatorSequence.class );
        if (!responseMessage.isStatus()) {
            return responseMessage;
        }


        ResponseMessage eventResponse= generalService.findEventByIdOrEventKey(eventIdentifier);
        Event event = (Event)  eventResponse.getReturnObject();

        ResponseMessage userResponse = generalService.findUserById(survey.getUserId());
        User user = (User) userResponse.getReturnObject();

        survey.setEventId(event.getId());
        survey.setUserId(user.getId());

        // mongodb embedded elemanlar icin id olusturmaz. biz olusturuyoruz. sadece app-prodda calisir.
        if(Arrays.stream(environment.getActiveProfiles()).anyMatch(s -> s.contains("app-prod"))) {
            survey.setEventId(event.getExecutiveUser());
            survey.setUserId(user.getId());
            survey.getQuestions().forEach(question -> {
                question.setId(new ObjectId().toString());
                question.getOptions()
                        .forEach(questionOption -> questionOption.setId(new ObjectId().toString()));
            });

            survey.getQuestions().forEach(question -> {
                question.setTotalVoters(0);
                question.getOptions()
                        .forEach(questionOption -> questionOption.setVoters(0));
            });
        }

        survey.setParticipants(0);
        survey.setViewers(0);
        surveyRepository.save(survey);

        String message = surveyMessageProvider.getMessage("surveyCreatedSuccessfully");
        return new ResponseMessage(true, message, survey);

    }
    @Transactional
    public ResponseMessage deleteSurvey(String userId, String surveyId) {
        ResponseMessage responseMessage = new ResponseMessage();

        generalService.findUserById(userId);
        ResponseMessage surveyResponseMessage =  generalService.findSurveyById(surveyId);
        Survey survey = (Survey) surveyResponseMessage.getReturnObject();

        responseMessage.setReturnObject(survey);
        responseMessage.setStatus(true);

        List<Answer> answers = (List<Answer>)  answerService.getSurveyAnswers(survey.getEventId(), surveyId).getReturnObject();
        answerService.removeAnswers(answers);
        surveyRepository.delete(surveyId);

        responseMessage.setMessage(surveyMessageProvider.getMessage("surveyDeletedSuccessfully"));
        return responseMessage;
    }

    @Transactional
    public ResponseMessage updateSurvey(Survey survey, String eventIdentifier) {
        ResponseMessage responseMessage;
        List<Object> validatingObjects = new ArrayList<>();
        /* validate edilecek objeleri ekle. */
        validatingObjects.add(survey);
        survey.getQuestions().forEach(validatingObjects::add);

        survey.getQuestions().forEach(question -> question.getOptions().forEach(validatingObjects::add));
        responseMessage = surveyValidator.validate(validatingObjects, SurveyValidatorSequence.class);
        if (!responseMessage.isStatus()) {
            return responseMessage;
        }

        generalService.findSurveyById(survey.getId());
        Event event = (Event) generalService.findEventByIdOrEventKey(eventIdentifier).getReturnObject();
        generalService.findUserById(event.getExecutiveUser());

        surveyRepository.save(survey);
        String message = surveyMessageProvider.getMessage("surveyUpdatedSuccessfully");
        return new ResponseMessage(true, message, survey);

    }

    public ResponseMessage getSurveys(String eventId) {
        ResponseMessage responseMessage = new ResponseMessage();
        //check if event exists.
        generalService.findEventByIdOrEventKey(eventId);

        List<Survey> surveys = surveyRepository.findSurveysByEventId(eventId);
        responseMessage.setStatus(true);
        responseMessage.setMessage(surveyMessageProvider.getMessage("surveysListedSuccessfully"));
        responseMessage.setReturnObject(surveys);
        return responseMessage;
    }

    public ResponseMessage getSurvey(String surveyId) {
        return generalService.findSurveyById(surveyId);
    }




}
