package javaday.istanbul.sliconf.micro.service.comment;

import javaday.istanbul.sliconf.micro.model.event.Comment;
import javaday.istanbul.sliconf.micro.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CommentRepositoryService implements CommentService {


    @Autowired
    private CommentRepository repo;

    public Comment save(Comment comment) {
        return repo.save(comment);
    }

    public List<Comment> findAllByEventIdAndSessionIdAndUserId(String eventId, String sessionId, String userId) {
        List<Comment> comments = repo.findAllByEventIdAndSessionIdAndUserId(eventId, sessionId, userId);

        return Objects.nonNull(comments) ? comments : new ArrayList<>();
    }


    public List<Comment> findAllByEventIdAndSessionIdAndUserIdAndStatus(String eventId, String sessionId, String userId, String status) {
        List<Comment> comments = repo.findAllByEventIdAndSessionIdAndUserIdAndApproved(eventId, sessionId, userId, status);

        return Objects.nonNull(comments) ? comments : new ArrayList<>();
    }

    public List<Comment> findAllByEventIdAndSessionId(String eventId, String sessionId) {
        List<Comment> comments = repo.findAllByEventIdAndSessionId(eventId, sessionId);

        return Objects.nonNull(comments) ? comments : new ArrayList<>();
    }

    public List<Comment> findAllByEventId(String eventId) {
        List<Comment> comments = repo.findAllByEventId(eventId);

        return Objects.nonNull(comments) ? comments : new ArrayList<>();
    }

    public Comment findById(String commentId) {
        return repo.findOne(commentId);
    }
}