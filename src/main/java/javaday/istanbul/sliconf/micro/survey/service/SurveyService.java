package javaday.istanbul.sliconf.micro.survey.service;

import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.survey.SurveyMessageProvider;
import javaday.istanbul.sliconf.micro.survey.SurveyRepository;
import javaday.istanbul.sliconf.micro.survey.model.Answer;
import javaday.istanbul.sliconf.micro.survey.model.Survey;
import javaday.istanbul.sliconf.micro.survey.util.SurveyUtil;
import javaday.istanbul.sliconf.micro.survey.validator.SurveyValidator;
import javaday.istanbul.sliconf.micro.survey.validator.SurveyValidatorSequence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class SurveyService {

    private final GeneralService generalService;
    private final SurveyValidator surveyValidator;
    private final SurveyRepository surveyRepository;
    private final SurveyMessageProvider surveyMessageProvider;

    /* Answer service surveyservice'e surveyservice ise answerService' bağımlı.
       bu sebeple bunları @Autowired ile lazy load ediyoruz. böylece circular dependency hatası almıyoruz.
    */
    @Autowired
    private AnswerService answerService;

    @Transactional
    public ResponseMessage addNewSurvey(Survey survey, String eventIdentifier) {

        ResponseMessage eventResponse = generalService.findEventByIdOrEventKey(eventIdentifier);
        Event event = (Event) eventResponse.getReturnObject();

        survey.setEventId(event.getId());
        survey.setEventKey(event.getKey());

        ResponseMessage userResponse = generalService.findUserById(survey.getUserId());
        User user = (User) userResponse.getReturnObject();

        SurveyUtil.generateDates(survey, event);
        survey.setUserId(user.getId());

        survey.setParticipants(0);
        survey.setViewers(0);
        survey.setViewerList(new ArrayList<>());

        SurveyUtil.generateQuestionIds(survey);

        List<Object> validatingObjects = new ArrayList<>();
        // validate edilecek objeleri ekle.
        validatingObjects.add(survey);
        validatingObjects.addAll(survey.getQuestions());

        survey.getQuestions().forEach(question -> validatingObjects.addAll(question.getOptions()));
        ResponseMessage responseMessage;

        responseMessage = surveyValidator.validate(validatingObjects, SurveyValidatorSequence.class);

        if (!responseMessage.isStatus()) {
            return responseMessage;
        }
        // etkinlik baslamis ise anket otomatik olarak aktif olur.
        if(event.getStartDate().isBefore(LocalDateTime.now()))
            survey.setIsActive(true);
        surveyRepository.save(survey);

        String message = surveyMessageProvider.getMessage("surveyCreatedSuccessfully");
        return new ResponseMessage(true, message, survey);

    }

    @Transactional
    public ResponseMessage deleteSurvey(String surveyId) {
        ResponseMessage responseMessage = new ResponseMessage();
        ResponseMessage surveyResponseMessage = generalService.findSurveyById(surveyId);
        Survey survey = (Survey) surveyResponseMessage.getReturnObject();

        responseMessage.setReturnObject(survey);
        responseMessage.setStatus(true);

        List<Answer> answers = (List<Answer>) answerService.getSurveyAnswers(survey.getEventId(), surveyId).getReturnObject();
        answerService.removeAnswers(answers);
        surveyRepository.delete(surveyId);

        responseMessage.setMessage(surveyMessageProvider.getMessage("surveyDeletedSuccessfully"));
        return responseMessage;
    }

    @Transactional
    public ResponseMessage updateSurvey(Survey survey, String eventIdentifier) {
        ResponseMessage responseMessage;

        Event event = (Event) generalService.findEventByIdOrEventKey(eventIdentifier).getReturnObject();
        generalService.findUserById(event.getExecutiveUser());

        survey.setEventId(event.getId());
        survey.setEventKey(event.getKey());

        SurveyUtil.generateDates(survey, event);
        SurveyUtil.generateQuestionIds(survey);

        List<Object> validatingObjects = new ArrayList<>();
        /* validate edilecek objeleri ekle. */
        validatingObjects.add(survey);
        validatingObjects.addAll(survey.getQuestions());

        survey.getQuestions().forEach(question -> validatingObjects.addAll(question.getOptions()));
        responseMessage = surveyValidator.validate(validatingObjects, SurveyValidatorSequence.class);
        if (!responseMessage.isStatus()) {
            return responseMessage;
        }

        generalService.findSurveyById(survey.getId());
        surveyRepository.save(survey);
        String message = surveyMessageProvider.getMessage("surveyUpdatedSuccessfully");
        return new ResponseMessage(true, message, survey);

    }

    @Transactional
    public ResponseMessage getSurveys(String eventIdentifier) {
        ResponseMessage responseMessage = new ResponseMessage();
        //check if event exists.
        generalService.findEventByIdOrEventKey(eventIdentifier);

        List<Survey> surveys = surveyRepository.findSurveysByEventId(eventIdentifier);
        if (surveys.isEmpty())
            surveys = surveyRepository.findSurveysByEventKey(eventIdentifier);
        responseMessage.setStatus(true);
        responseMessage.setMessage(surveyMessageProvider.getMessage("surveysListedSuccessfully"));
        responseMessage.setReturnObject(surveys);
        return responseMessage;
    }

    public ResponseMessage getSurvey(String surveyId) {
        return generalService.findSurveyById(surveyId);
    }

    @Transactional
    public ResponseMessage updateSurveyViewers(String userId, String surveyId, String eventIdentifier) {
        ResponseMessage responseMessage = new ResponseMessage();
        Survey survey = (Survey) generalService.findSurveyById(surveyId).getReturnObject();
        generalService.findUserById(userId);
        List<String> viewers = new ArrayList<>(survey.getViewerList());
        if (viewers.stream().noneMatch(s -> s.equals(userId))) {
            viewers.add(userId);
            survey.setViewerList(viewers);
            survey.setViewers(viewers.size());
            this.updateSurvey(survey, eventIdentifier);
            responseMessage.setMessage(surveyMessageProvider.getMessage("userHasAddedSurveyViewerList"));
            responseMessage.setStatus(true);
            responseMessage.setReturnObject(userId);
        } else {
            responseMessage.setReturnObject(userId);
            responseMessage.setStatus(false);
            responseMessage.setMessage(surveyMessageProvider.getMessage("userAlreadyViewedSurvey"));
        }
        return responseMessage;
    }


}
