package javaday.istanbul.sliconf.micro.user.schedule;

import javaday.istanbul.sliconf.micro.user.model.UserScheduleElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile({"prod", "dev"})
public class UserScheduleRepositoryService implements UserScheduleService {

    @Autowired
    protected UserScheduleRepository repo;


    public UserScheduleElement save(UserScheduleElement userScheduleElement) {
        return repo.save(userScheduleElement);
    }

    public UserScheduleElement findById(String id) {
        return repo.findOne(id);
    }

    public List<UserScheduleElement> findAll() {
        return repo.findAll();
    }

    public List<UserScheduleElement> findByUserIdAndEventId(String userId, String eventId) {
        return repo.findByUserIdAndEventId(userId, eventId);
    }

    public UserScheduleElement findByUserIdAndEventIdAndSessionId(String userId, String eventId, String sessionId) {
        return repo.findByUserIdAndEventIdAndSessionId(userId, eventId, sessionId);
    }

    public void deleteByIdAndEventIdAndSessionIdAndUserId(String id, String eventId, String sessionId, String userId) {
        repo.deleteByIdAndEventIdAndSessionIdAndUserId(id, eventId, sessionId, userId);
    }

    public UserScheduleElement deleteByEventIdAndSessionIdAndUserId(String eventId, String sessionId, String userId) {
        return repo.deleteByEventIdAndSessionIdAndUserId(eventId, sessionId, userId);
    }

    public void deleteById(String id) {
        repo.deleteById(id);
    }
}