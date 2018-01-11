package javaday.istanbul.sliconf.micro.service.comment;

import javaday.istanbul.sliconf.micro.model.event.Comment;
import javaday.istanbul.sliconf.micro.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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


    public List<Comment> findFirstByApprovedAndEventIdOrderByRateDesc(String status, String eventId, int count) {
        List<Comment> comments = repo.findFirstByApprovedAndEventIdOrderByRateDesc(status, eventId, count);

        return Objects.nonNull(comments) ? comments : new ArrayList<>();
    }

    public List<Comment> findAllByStatusAndEventIdAndSessionIdAndUserId(String status, String eventId, String sessionId, String userId, int count, String type) {
        List<Comment> comments;

        if (count != 0) {
            if ("top-rated".equals(type)) {
                comments = repo.findFirstByApprovedAndEventIdAndSessionIdAndUserIdOrderByRateDesc(status, eventId, sessionId, userId, count);
            } else if ("recent".equals(type)) {
                comments = repo.findFirstByApprovedAndEventIdAndSessionIdAndUserIdOrderByTimeDesc(status, eventId, sessionId, userId, count);
            } else {
                comments = repo.findFirstByApprovedAndEventIdAndSessionIdAndUserId(status, eventId, sessionId, userId, count);
            }
        } else {
            if ("top-rated".equals(type)) {
                comments = repo.findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByRateDesc(status, eventId, sessionId, userId);
            } else if ("recent".equals(type)) {
                comments = repo.findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByTimeDesc(status, eventId, sessionId, userId);
            } else {
                comments = repo.findAllByApprovedAndEventIdAndSessionIdAndUserId(status, eventId, sessionId, userId);
            }
        }

        return Objects.nonNull(comments) ? comments : new ArrayList<>();
    }

    public List<Comment> findAllByStatusAndEventIdAndSessionId(String status, String eventId, String sessionId, int count, String type) {
        List<Comment> comments;

        if (count != 0) {
            if ("top-rated".equals(type)) {
                comments = repo.findFirstByApprovedAndEventIdAndSessionIdOrderByRateDesc(status, eventId, sessionId, count);
            } else if ("recent".equals(type)) {
                comments = repo.findFirstByApprovedAndEventIdAndSessionIdOrderByTimeDesc(status, eventId, sessionId, count);
            } else {
                comments = repo.findFirstByApprovedAndEventIdAndSessionId(status, eventId, sessionId, count);
            }
        } else {
            if ("top-rated".equals(type)) {
                comments = repo.findAllByApprovedAndEventIdAndSessionIdOrderByRateDesc(status, eventId, sessionId);
            } else if ("recent".equals(type)) {
                comments = repo.findAllByApprovedAndEventIdAndSessionIdOrderByTimeDesc(status, eventId, sessionId);
            } else {
                comments = repo.findAllByApprovedAndEventIdAndSessionId(status, eventId, sessionId);
            }
        }

        return Objects.nonNull(comments) ? comments : new ArrayList<>();
    }

    public List<Comment> findAllByStatusAndEventId(String status, String eventId, int count, String type) {
        List<Comment> comments;

        if (count != 0) {
            if ("top-rated".equals(type)) {
                comments = repo.findFirstByApprovedAndEventIdOrderByRateDesc(status, eventId, count);
            } else if ("recent".equals(type)) {
                comments = repo.findFirstByApprovedAndEventIdOrderByTimeDesc(status, eventId, count);
            } else {
                comments = repo.findFirstByApprovedAndEventId(status, eventId, count);
            }
        } else {
            if ("top-rated".equals(type)) {
                comments = repo.findAllByApprovedAndEventIdOrderByRateDesc(status, eventId);
            } else if ("recent".equals(type)) {
                comments = repo.findAllByApprovedAndEventIdOrderByTimeDesc(status, eventId);
            } else {
                comments = repo.findAllByApprovedAndEventId(status, eventId);
            }
        }

        return Objects.nonNull(comments) ? comments : new ArrayList<>();
    }

    public Comment findMostLikedComment(String status, String eventId) {
        Comment comment = repo.findMostLikedComment(status, eventId);

        return Objects.nonNull(comment) ? comment : new Comment();
    }

    public String findMostCommentedSessionId(String status, String eventId) {
        List<Comment> comments = repo.findAllByApprovedAndEventId(status, eventId);

        Map<String, Long> countBySessionId = new HashMap<>();

        if (Objects.nonNull(comments)) {
            comments.forEach(comment -> {
                if (Objects.nonNull(comment) && Objects.nonNull(comment.getSessionId())) {
                    Long count = countBySessionId.get(comment.getSessionId());

                    if (Objects.nonNull(count)) {
                        count = count + 1;
                    } else {
                        count = 0L;
                    }
                    countBySessionId.put(comment.getSessionId(), count);
                }
            });
        }

        String sessionId = "";

        long count = 0;

        for (Map.Entry<String, Long> entry : countBySessionId.entrySet()) {
            if (Objects.nonNull(entry) && Objects.nonNull(entry.getValue()) && count < entry.getValue()) {
                count = entry.getValue();
                sessionId = entry.getKey();
            }
        }

        return Objects.nonNull(sessionId) ? sessionId : "";
    }

    public Comment findById(String commentId) {
        return repo.findOne(commentId);
    }
}