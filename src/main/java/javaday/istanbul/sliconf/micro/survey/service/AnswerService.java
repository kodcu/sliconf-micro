package javaday.istanbul.sliconf.micro.survey.service;

import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.survey.*;
import javaday.istanbul.sliconf.micro.survey.model.Answer;
import javaday.istanbul.sliconf.micro.survey.model.Question;
import javaday.istanbul.sliconf.micro.survey.model.QuestionOption;
import javaday.istanbul.sliconf.micro.survey.model.Survey;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

@AllArgsConstructor
@Slf4j
@Service
public class AnswerService {
    private final GeneralService generalService;
    private final SurveyValidator surveyValidator;
    private final SurveyMessageProvider surveyMessageProvider;
    private final AnswerRepository answerRepository;
    private final SurveyService surveyService;

    public ResponseMessage answerSurvey(Answer answer, String userId, String surveyId) {
        ResponseMessage responseMessage;

        generalService.findUserById(answer.getUserId());

        List<Object> validatingObjects = new ArrayList<>();
        validatingObjects.add(answer);
        responseMessage = surveyValidator.validate(validatingObjects);
        if (!responseMessage.isStatus()) {
            return responseMessage;
        }

        Survey survey = (Survey) generalService.findSurveyById(surveyId).getReturnObject();

        boolean alreadyAnswered = this.checkIfUserAlreadyAnsweredSurvey(userId, surveyId).isStatus();
        if (alreadyAnswered) {
            responseMessage.setStatus(false);
            responseMessage.setMessage(surveyMessageProvider.getMessage("surveyAlreadyAnswered"));
            responseMessage.setReturnObject(answer);
            return responseMessage;
        }

        this.checkAnsweredQuestionsExists(survey, answer);
        this.updateSurveyVoteCount(answer, survey);

        surveyService.updateSurvey(survey);
        answerRepository.save(answer);

        responseMessage.setStatus(true);
        responseMessage.setMessage(surveyMessageProvider.getMessage("surveyAnsweredSuccessfully"));
        responseMessage.setReturnObject(answer);
        return responseMessage;
    }

    public ResponseMessage updateSurveyAnswers(Answer answer) {
        ResponseMessage responseMessage;

        generalService.findUserById(answer.getUserId());
        this.findAnswerById(answer.getId());
        generalService.findSurveyById(answer.getSurveyId());

        List<Object> validatingObjects = new ArrayList<>();
        validatingObjects.add(answer);
        responseMessage = surveyValidator.validate(validatingObjects);
        if (!responseMessage.isStatus()) {
            return responseMessage;
        }

        answerRepository.save(answer);

        responseMessage.setStatus(true);
        responseMessage.setMessage(surveyMessageProvider.getMessage("surveyAnsweredSuccessfully"));
        responseMessage.setReturnObject(answer);
        return responseMessage;
    }

    public ResponseMessage getSurveyAnswers(String eventKey, String surveyId) {
        ResponseMessage responseMessage = new ResponseMessage();
        //check if event exists.
        generalService.findEventByKey(eventKey);
        generalService.findSurveyById(surveyId);

        List<Answer> answers = answerRepository.findAnswersByEventKeyAndSurveyId(eventKey, surveyId);
        responseMessage.setStatus(true);
        responseMessage.setMessage(surveyMessageProvider.getMessage("answersListedSuccessfully"));
        responseMessage.setReturnObject(answers);
        return responseMessage;
    }

    private void updateSurveyVoteCount(Answer answer, Survey survey) {

        answer.getAnsweredQuestions().forEach((answeredQuestionId, answeredOption) -> {

            Predicate<Question> questionPredicate;
            Predicate<QuestionOption> optionPredicate;
            questionPredicate = surveyQuestion -> surveyQuestion.getId().equals(answeredQuestionId);
            optionPredicate = questionOption -> questionOption.getText().equals(answeredOption);
            survey.getQuestions().stream()
                    .filter(questionPredicate)
                    .forEach(question -> question.setTotalVoters(question.getTotalVoters() + 1));

            survey.getQuestions()
                    .forEach(question -> question.getOptions().stream().filter(optionPredicate)
                    .forEach(questionOption -> questionOption.setVoters(questionOption.getVoters() + 1)));
        });
    }

    private void checkAnsweredQuestionsExists(Survey survey, Answer answer) {

        answer.getAnsweredQuestions().forEach((answeredQuestionId, answeredOption) -> {

            Predicate<Question> questionPredicate;
            questionPredicate = surveyQuestion -> surveyQuestion.getId().equals(answeredQuestionId);

            survey.getQuestions().stream()
                    .filter(questionPredicate)
                    .findAny().orElseThrow(() -> {
                log.error("Question not found by id: {}", answeredQuestionId);
                String message = surveyMessageProvider.getMessage("questionCanNotFoundWithGivenId");
                return new SurveyException(message, answeredQuestionId);

            });
        });
    }

    private ResponseMessage checkIfUserAlreadyAnsweredSurvey(String userId, String surveyId) {

        ResponseMessage responseMessage = new ResponseMessage();
        Answer answer = answerRepository.findByUserIdAndSurveyId(userId, surveyId).orElse(null);
        if (Objects.isNull(answer))
            responseMessage.setStatus(false);
        else
            responseMessage.setStatus(true);
        return responseMessage;
    }


    private void findAnswerById(String answerId) {
        answerRepository.findById(answerId).orElseThrow(() -> {
            log.error("Answer not found by id: {}", answerId);
            String message = surveyMessageProvider.getMessage("answerCanNotFoundWithGivenId");
            return new SurveyException(message, answerId);
        });


    }
}
