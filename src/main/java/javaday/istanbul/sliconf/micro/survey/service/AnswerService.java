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
        Event event = (Event) generalService.findEventByIdOrEventKey(answer.getEventId()).getReturnObject();

        List<Object> validatingObjects = new ArrayList<>();
        validatingObjects.add(answer);
        responseMessage = surveyValidator.validate(validatingObjects, SurveyValidatorSequence.class);
        if (!responseMessage.isStatus()) {
            return responseMessage;
        }

        Survey survey = (Survey) generalService.findSurveyById(surveyId).getReturnObject();
        generalService.findEventByIdOrEventKey(survey.getEventId());

        // TODO: 17.09.2018 kullanıcı eğer anketin belirli sorularına cevap verdikten
        // TODO: sonra cevap vermediği bir soruyu cevaplamak
        // TODO: isterse zaten cevap verdin hatası alacaktır. bunu düzelt.
        boolean alreadyAnswered = this.checkIfUserAlreadyAnsweredSurvey(answer.getUserId(), surveyId).isStatus();
        if (alreadyAnswered) {
            responseMessage.setStatus(false);
            responseMessage.setMessage(surveyMessageProvider.getMessage("surveyAlreadyAnswered"));
            responseMessage.setReturnObject(answer);
            return responseMessage;
        }

        this.checkAnsweredQuestions(survey, answer);
        this.updateSurveyVoteCount(answer, survey);

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

    private void updateSurveyVoteCount(Answer answer, Survey survey) {

        survey.setParticipants(survey.getParticipants() + 1);

        answer.getAnsweredQuestions().forEach((answeredQuestionId, answeredOption) -> {

            Predicate<Question> questionPredicate;
            Predicate<QuestionOption> optionPredicate;
            questionPredicate = surveyQuestion -> surveyQuestion.getId().equals(answeredQuestionId);
            optionPredicate = questionOption -> questionOption.getText().equals(answeredOption);

            survey.getQuestions()
                    .stream()
                    .filter(questionPredicate)
                    .forEach(question -> {
                        question.setTotalVoters(question.getTotalVoters() + 1);
                        question.getOptions()
                                .stream()
                                .filter(optionPredicate)
                                .forEach(questionOption -> questionOption.setVoters(questionOption.getVoters() + 1));
                    });
        });
    }

    private void checkAnsweredQuestions(Survey survey, Answer answer) {

        answer.getAnsweredQuestions().forEach((answeredQuestionId, answeredOption) -> {
            Predicate<Question> questionPredicate;
            questionPredicate = surveyQuestion -> surveyQuestion.getId().equals(answeredQuestionId);

            if (survey.getQuestions().stream().noneMatch(questionPredicate)) {
                log.error("Question not found by id: {}", answeredQuestionId);
                String message = surveyMessageProvider.getMessage("questionCanNotFoundWithGivenId");
                throw new SurveyException(message, answeredQuestionId);
            }

            Predicate<QuestionOption> questionOptionPredicate;
            questionOptionPredicate = questionOption -> questionOption.getText().equals(answeredOption);

            survey.getQuestions()
                    .stream()
                    .filter(questionPredicate)
                    .forEach(question -> {
                        if (question.getOptions().stream().noneMatch(questionOptionPredicate)) {
                            log.error("Answer does not match with any question option: {}", answeredOption);
                            String message = surveyMessageProvider.getMessage("questionAndAnswerMismatch");
                            throw new SurveyException(message, answeredOption);
                        }
                    });
        });
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
