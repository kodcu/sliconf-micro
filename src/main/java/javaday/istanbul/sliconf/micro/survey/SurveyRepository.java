package javaday.istanbul.sliconf.micro.survey;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SurveyRepository extends MongoRepository<Survey, String> {

    Optional<Survey> findById(String id);

    List<Survey> findSurveysByEventIdAndSessionId(String eventId, String sessionId);
}
