package javaday.istanbul.sliconf.micro.survey;

import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.event.agenda.AgendaElement;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.CommentMessageProvider;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Slf4j
@AllArgsConstructor
@Service
public class SurveyService {

    private final SurveyValidator surveyValidator;
    private final UserRepositoryService userRepositoryService;
    private final EventRepositoryService eventRepositoryService;
    private final CommentMessageProvider commentMessageProvider;
    private final SurveyMessageProvider surveyMessageProvider;
    private final SurveyRepository surveyRepository;


    ResponseMessage addNewSurvey(Survey survey) throws RuntimeException {

        ResponseMessage responseMessage;
        responseMessage = surveyValidator.validate(survey);
        if (!responseMessage.isStatus()) {
            return responseMessage;
        }

        ResponseMessage eventResponse= this.findEventById(survey.getEventId());
        Event event = (Event)  eventResponse.getReturnObject();

        ResponseMessage userResponse = this.findUserById(survey.getUserId());
        User user = (User) userResponse.getReturnObject();

        ResponseMessage agendaResponse = getAgendaElement(event.getAgenda(), survey.getSessionId());

        if (!agendaResponse.isStatus()) {
            agendaResponse.setReturnObject(survey);
            return agendaResponse;
        }

        AgendaElement agendaElement = (AgendaElement) agendaResponse.getReturnObject();

        // TODO: 10.08.2018 Validate questions somehow before save.
        survey.setSessionId(agendaElement.getId());
        survey.setEventId(event.getId());
        survey.setUserId(user.getId());
        survey.setAnswers(new ArrayList<>());
        survey.setTime(LocalDateTime.now());

        surveyRepository.save(survey);

        String message = surveyMessageProvider.getMessage("surveyCreatedSuccessfully");
        return new ResponseMessage(true, message, survey);

    }

    ResponseMessage deleteSurvey(String userId, String surveyId) {
        ResponseMessage responseMessage = new ResponseMessage();

        this.findUserById(userId);
        ResponseMessage surveyResponseMessage =  this.findSurveyById(surveyId);
        Survey survey = (Survey) surveyResponseMessage.getReturnObject();

        responseMessage.setReturnObject(survey);
        responseMessage.setStatus(true);
        responseMessage.setMessage(surveyMessageProvider.getMessage("surveyDeletedSuccessfully"));

        surveyRepository.delete(surveyId);
        return responseMessage;
    }

    ResponseMessage answerSurvey(Answer answer, String userId, String surveyId) {
        ResponseMessage responseMessage;

        this.findUserById(answer.getUserId());

        responseMessage = surveyValidator.validate(answer);
        if (!responseMessage.isStatus()) {
            return responseMessage;
        }

        this.findUserById(userId);
        Survey survey = (Survey) this.findSurveyById(surveyId).getReturnObject();

        List<Answer> answers = survey.getAnswers();
        Predicate<Answer> answerPredicate = answer1 -> answer.getUserId().equals(answer1.getUserId());
        boolean alreadyAnswered = answers.stream().anyMatch(answerPredicate);
        if (alreadyAnswered) {
            responseMessage.setStatus(false);
            responseMessage.setMessage(surveyMessageProvider.getMessage("surveyAlreadyAnswered"));
            responseMessage.setReturnObject(answer);
            return responseMessage;
        }
        answers.add(answer);
        survey.setAnswers(answers);
        surveyRepository.save(survey);

        responseMessage.setStatus(true);
        responseMessage.setMessage(surveyMessageProvider.getMessage("surveyAnsweredSuccessfully"));
        responseMessage.setReturnObject(answer);
        return responseMessage;
    }

    ResponseMessage getSurveysByEventIdAndSessionId(String eventId, String sessionId) {
        ResponseMessage responseMessage = new ResponseMessage();
        Event event = (Event) this.findEventById(eventId).getReturnObject();
        ResponseMessage agendaResponse = getAgendaElement(event.getAgenda(), sessionId);
        if (!agendaResponse.isStatus()) {
            agendaResponse.setReturnObject(sessionId);
            return agendaResponse;
        }

        List<Survey> surveys = surveyRepository.findSurveysByEventIdAndSessionId(eventId, sessionId);
        responseMessage.setStatus(false);
        responseMessage.setMessage(surveyMessageProvider.getMessage("surveysListedSuccessfully"));
        responseMessage.setReturnObject(surveys);
        return responseMessage;
    }

    ResponseMessage updateSurvey(Survey survey) {

        ResponseMessage responseMessage;
        responseMessage = surveyValidator.validate(survey);
        if (!responseMessage.isStatus()) {
            return responseMessage;
        }
         this.findSurveyById(survey.getId());
         this.findUserById(survey.getUserId());
         Event event = (Event) this.findEventById(survey.getEventId()).getReturnObject();

        ResponseMessage agendaResponse = getAgendaElement(event.getAgenda(), survey.getSessionId());
        if (!agendaResponse.isStatus()) {
            agendaResponse.setReturnObject(survey);
            return agendaResponse;
        }

        surveyRepository.save(survey);
        String message = surveyMessageProvider.getMessage("surveyUpdatedSuccessfully");
        return new ResponseMessage(true, message, survey);

    }

    private ResponseMessage findSurveyById(String surveyId){

        ResponseMessage responseMessage = new ResponseMessage();
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> {
            log.error("survey not found by id: {}", surveyId);
            String message = surveyMessageProvider.getMessage("surveyCanNotFoundWithGivenId");
            return new SurveyException(message, surveyId);
        });
        responseMessage.setReturnObject(survey);
        return responseMessage;
    }

    private ResponseMessage findEventById(String eventId){
        ResponseMessage responseMessage = new ResponseMessage();
        Event event = eventRepositoryService.findById(eventId).orElseThrow(() -> {
            log.error("event not found by id: {}", eventId);
            String message = surveyMessageProvider.getMessage("eventCanNotFoundWithGivenId");
            return new SurveyException(message, eventId);
        });
        responseMessage.setReturnObject(event);
        return responseMessage;
    }

    private ResponseMessage findUserById(String userId) {
        ResponseMessage responseMessage = new ResponseMessage();
        User user = userRepositoryService.findById(userId).orElseThrow(() -> {
            log.error("User not found by id: {}", userId);
            String message = surveyMessageProvider.getMessage("userCanNotFoundWithGivenId");
            return new SurveyException(message, userId);
        });
        responseMessage.setReturnObject(user);
        return responseMessage;
    }

    private ResponseMessage getAgendaElement(List<AgendaElement> agendaElements, String agendaElementId) {
        if (Objects.nonNull(agendaElements)) {
            AgendaElement agendaElementReturn = agendaElements
                    .stream()
                    .filter(agendaElement -> Objects.nonNull(agendaElement) &&
                            Objects.nonNull(agendaElement.getId()) &&
                            agendaElement.getId().equals(agendaElementId)).findFirst().orElse(null);

            if (Objects.nonNull(agendaElementReturn)) {
                return new ResponseMessage(true, commentMessageProvider.getMessage("agendaElementFoundWithGivenId"), agendaElementReturn);
            }
        }

        return new ResponseMessage(false, commentMessageProvider.getMessage("agendaElementCanNotFoundWithGivenId"), agendaElementId);
    }


}
