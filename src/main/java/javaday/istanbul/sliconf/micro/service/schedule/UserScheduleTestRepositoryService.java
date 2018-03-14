package javaday.istanbul.sliconf.micro.service.schedule;

import javaday.istanbul.sliconf.micro.model.UserScheduleElement;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Profile("test")
public class UserScheduleTestRepositoryService extends UserScheduleRepositoryService {

    @Override
    public void deleteById(String id) {
        repo.delete(id);
    }

    @Override
    public void deleteByIdAndEventIdAndSessionIdAndUserId(String id, String eventId, String sessionId, String userId) {
        UserScheduleElement userScheduleElement = repo.findByUserIdAndEventIdAndSessionId(userId, eventId, sessionId);

        if (Objects.nonNull(userScheduleElement) && Objects.nonNull(userScheduleElement.getId())
                && userScheduleElement.getId().equals(id)) {
            this.deleteById(userScheduleElement.getId());
        }
    }
}