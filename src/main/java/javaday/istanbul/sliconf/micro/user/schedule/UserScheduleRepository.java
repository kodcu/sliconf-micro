package javaday.istanbul.sliconf.micro.user.schedule;

import javaday.istanbul.sliconf.micro.user.model.UserScheduleElement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserScheduleRepository extends MongoRepository<UserScheduleElement, String>,
        CrudRepository<UserScheduleElement, String> {

    UserScheduleElement findById(String id);

    UserScheduleElement findByUserIdAndEventIdAndSessionId(String userId, String eventId, String sessionId);

    void deleteByIdAndEventIdAndSessionIdAndUserId(String id, String eventId, String sessionId, String userId);

    UserScheduleElement deleteByEventIdAndSessionIdAndUserId(String eventId, String sessionId, String userId);

    List<UserScheduleElement> findByUserIdAndEventId(String userId, String eventId);

    void deleteById(String id);
}