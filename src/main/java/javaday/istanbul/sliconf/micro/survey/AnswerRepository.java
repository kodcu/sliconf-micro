package javaday.istanbul.sliconf.micro.survey;

import javaday.istanbul.sliconf.micro.survey.model.Answer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends MongoRepository<Answer, String> {

    Optional<Answer> findByUserIdAndSurveyId(String userId, String surveyId);

    List<Answer> findAnswersByEventIdAndSurveyId(String eventId, String surveyId);

    Optional<Answer> findById(String id);
}
