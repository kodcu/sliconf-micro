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


    public List<Comment> findAllByStatusAndEventIdAndSessionIdAndUserId(String status, String eventId, String sessionId, String userId) {
        List<Comment> comments = repo.findAllByApprovedAndEventIdAndSessionIdAndUserId(status,eventId, sessionId, userId);

        return Objects.nonNull(comments) ? comments : new ArrayList<>();
    }

    public List<Comment> findAllByStatusAndEventIdAndSessionId(String status, String eventId, String sessionId) {
        List<Comment> comments = repo.findAllByApprovedAndEventIdAndSessionId(status, eventId, sessionId);

        return Objects.nonNull(comments) ? comments : new ArrayList<>();
    }

    public List<Comment> findAllByStatusAndEventId(String status, String eventId) {
        List<Comment> comments = repo.findAllByApprovedAndEventId(status, eventId);

        return Objects.nonNull(comments) ? comments : new ArrayList<>();
    }

    public Comment findById(String commentId) {
        return repo.findOne(commentId);
    }
}