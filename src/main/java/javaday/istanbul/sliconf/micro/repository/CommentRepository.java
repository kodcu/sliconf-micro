package javaday.istanbul.sliconf.micro.repository;

import javaday.istanbul.sliconf.micro.model.event.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends CrudRepository<Comment, String> {

    List<Comment> findAllByEventIdAndSessionIdAndUserIdAndApproved(String eventId, String sessionId, String userId, String approved);

    List<Comment> findAllByEventIdAndSessionIdAndUserId(String eventId, String sessionId, String userId);

    List<Comment> findAllByEventIdAndSessionId(String eventId, String sessionId);

    List<Comment> findAllByEventId(String eventId);
}