package javaday.istanbul.sliconf.micro.survey.service;

import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.survey.AnswerRepository;
import javaday.istanbul.sliconf.micro.survey.SurveyException;
import javaday.istanbul.sliconf.micro.survey.SurveyMessageProvider;
import javaday.istanbul.sliconf.micro.survey.model.Answer;
import javaday.istanbul.sliconf.micro.survey.model.Question;
import javaday.istanbul.sliconf.micro.survey.model.QuestionOption;
import javaday.istanbul.sliconf.micro.survey.model.Survey;
import javaday.istanbul.sliconf.micro.survey.util.SurveyUtil;
import javaday.istanbul.sliconf.micro.survey.validator.SurveyValidator;
import javaday.istanbul.sliconf.micro.survey.validator.SurveyValidatorSequence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Slf4j
@Service
public class AnswerService {
    private final GeneralService generalService;
    private final SurveyValidator surveyValidator;
    private final SurveyMessageProvider surveyMessageProvider;
    private final AnswerRepository answerRepository;

    /* Answer service surveyservice'e surveyservice ise answerService' bağımlı.
       bu sebeple bunları @Autowired ile lazy load ediyoruz. böylece circular dependency hatası almıyoruz.
    */
    @Autowired
    private SurveyService surveyService;

    @Transactional
    public ResponseMessage answerSurvey(Answer answer, String surveyId, String eventIdentifier) {
        ResponseMessage responseMessage;

        generalService.findUserById(answer.getUserId());
        Event event = (Event) generalService.findEventByIdOrEventKey(eventIdentifier).getReturnObject();
        answer.setEventId(event.getId());
        answer.setEventKey(event.getKey());

        Survey survey = (Survey) generalService.findSurveyById(surveyId).getReturnObject();
        answer.setSurveyId(survey.getId());

        List<Object> validatingObjects = new ArrayList<>();
        validatingObjects.add(answer);
        responseMessage = surveyValidator.validate(validatingObjects, SurveyValidatorSequence.class);
        if (!responseMessage.isStatus()) {
            return responseMessage;
        }

        boolean alreadyAnswered = this.checkIfUserAlreadyAnsweredSurvey(answer.getUserId(), surveyId).isStatus();
        if (alreadyAnswered) {
            responseMessage.setStatus(false);
            responseMessage.setMessage(surveyMessageProvider.getMessage("surveyAlreadyAnswered"));
            responseMessage.setReturnObject(answer);
            return responseMessage;
        }

        SurveyUtil.checkAnsweredQuestions(survey, answer, surveyMessageProvider);
        SurveyUtil.updateSurveyVoteCount(answer, survey);

        surveyService.updateSurvey(survey, eventIdentifier);
        answerRepository.save(answer);

        responseMessage.setStatus(true);
        responseMessage.setMessage(surveyMessageProvider.getMessage("surveyAnsweredSuccessfully"));
        responseMessage.setReturnObject(answer);
        return responseMessage;
    }
    @Transactional
    public ResponseMessage updateSurveyAnswers(Answer answer) {
        ResponseMessage responseMessage;

        generalService.findUserById(answer.getUserId());
        this.findAnswerById(answer.getId());
        generalService.findSurveyById(answer.getSurveyId());

        List<Object> validatingObjects = new ArrayList<>();
        validatingObjects.add(answer);
        responseMessage = surveyValidator.validate(validatingObjects, SurveyValidatorSequence.class);
        if (!responseMessage.isStatus()) {
            return responseMessage;
        }

        answerRepository.save(answer);

        responseMessage.setStatus(true);
        responseMessage.setMessage(surveyMessageProvider.getMessage("surveyAnsweredSuccessfully"));
        responseMessage.setReturnObject(answer);
        return responseMessage;
    }

    @Transactional
    public ResponseMessage getSurveyAnswers(String eventIdentifier, String surveyId) {
        ResponseMessage responseMessage = new ResponseMessage();
        //check if event exists.
        generalService.findEventByIdOrEventKey(eventIdentifier);
        generalService.findSurveyById(surveyId);
        List<Answer> answers = answerRepository.findBySurveyId(surveyId);

        responseMessage.setStatus(true);
        responseMessage.setMessage(surveyMessageProvider.getMessage("answersListedSuccessfully"));
        responseMessage.setReturnObject(answers);
        return responseMessage;
    }

    // Kullanicinin bir etkinlikteki tüm anketlere vermis oldugu tüm cevaplari dondurur.
    public ResponseMessage getEventSurveysAnswersOfUser(String eventId, String userId) {
        ResponseMessage responseMessage = new ResponseMessage();
        List<Answer> answers = answerRepository.findByEventIdAndUserId(eventId, userId);
        responseMessage.setStatus(true);
        responseMessage.setReturnObject(answers);
        responseMessage.setMessage("Users answers of surveys in specific events listed, if any.");
        return  responseMessage;
    }



    private ResponseMessage checkIfUserAlreadyAnsweredSurvey(String userId, String surveyId) {

        ResponseMessage responseMessage = new ResponseMessage();
        Answer answer = answerRepository.findByUserIdAndSurveyId(userId, surveyId).orElse(null);

        responseMessage.setStatus(!Objects.isNull(answer));
        return responseMessage;
    }


    private void findAnswerById(String answerId) {
        if(!answerRepository.findById(answerId).isPresent()) {
            log.error("Answer not found by id: {}", answerId);
            String message = surveyMessageProvider.getMessage("answerCanNotFoundWithGivenId");
            throw  new SurveyException(message, answerId);
        }


    }

    // Removes answers of deleted survey.
    void removeAnswers(List<Answer> answers) {
        answerRepository.delete(answers);
    }
}
