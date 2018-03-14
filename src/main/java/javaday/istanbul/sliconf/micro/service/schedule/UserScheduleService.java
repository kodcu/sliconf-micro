package javaday.istanbul.sliconf.micro.service.schedule;

import javaday.istanbul.sliconf.micro.model.UserScheduleElement;

import java.util.List;

public interface UserScheduleService {

    UserScheduleElement save(UserScheduleElement userScheduleElement);

    UserScheduleElement findById(String id);

    List<UserScheduleElement> findAll();

    UserScheduleElement findByUserIdAndEventIdAndSessionId(String userId, String eventId, String sessionId);

    void deleteByIdAndEventIdAndSessionIdAndUserId(String id, String eventId, String sessionId, String userId);

    List<UserScheduleElement> findByUserIdAndEventId(String userId, String eventId);
}
