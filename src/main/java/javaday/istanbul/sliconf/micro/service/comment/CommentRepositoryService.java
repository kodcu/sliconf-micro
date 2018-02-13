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

    private static final String TOP_RATED = "top-rated";
    private static final String RECENT = "recent";
    private static final String OLDEST = "oldest";

    public Comment save(Comment comment) {
        return repo.save(comment);
    }

    public List<Comment> findAllByStatusAndEventIdAndSessionIdAndUserId(String status, String eventId, String sessionId, String userId, int count, String type, int page) {
        List<Comment> comments;

        if (count != 0) {
            if (TOP_RATED.equals(type)) {
                comments = repo.findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByRateDescPageable(status, eventId, sessionId, userId, page, count);
            } else if (RECENT.equals(type)) {
                comments = repo.findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByTimeDescPageable(status, eventId, sessionId, userId, page, count);
            } else if (OLDEST.equals(type)) {
                comments = repo.findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByTimeAscPageable(status, eventId, sessionId, userId, page, count);
            } else {
                comments = repo.findAllByApprovedAndEventIdAndSessionIdAndUserIdPageable(status, eventId, sessionId, userId, page, count);
            }
        } else {
            if (TOP_RATED.equals(type)) {
                comments = repo.findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByRateDesc(status, eventId, sessionId, userId);
            } else if (RECENT.equals(type)) {
                comments = repo.findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByTimeDesc(status, eventId, sessionId, userId);
            } else if (OLDEST.equals(type)) {
                comments = repo.findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByTimeAsc(status, eventId, sessionId, userId);
            } else {
                comments = repo.findAllByApprovedAndEventIdAndSessionIdAndUserId(status, eventId, sessionId, userId);
            }
        }

        return Objects.nonNull(comments) ? comments : new ArrayList<>();
    }

    public List<Comment> findAllByStatusAndEventIdAndSessionId(String status, String eventId, String sessionId, int count, String type, int page) {
        List<Comment> comments;

        if (count != 0) {
            if (TOP_RATED.equals(type)) {
                comments = repo.findAllByApprovedAndEventIdAndSessionIdOrderByRateDescPageable(status, eventId, sessionId, page, count);
            } else if (RECENT.equals(type)) {
                comments = repo.findAllByApprovedAndEventIdAndSessionIdOrderByTimeDescPageable(status, eventId, sessionId, page, count);
            } else if (OLDEST.equals(type)) {
                comments = repo.findAllByApprovedAndEventIdAndSessionIdOrderByTimeAscPageable(status, eventId, sessionId, page, count);
            } else {
                comments = repo.findAllByApprovedAndEventIdAndSessionIdPageable(status, eventId, sessionId, page, count);
            }
        } else {
            if (TOP_RATED.equals(type)) {
                comments = repo.findAllByApprovedAndEventIdAndSessionIdOrderByRateDesc(status, eventId, sessionId);
            } else if (RECENT.equals(type)) {
                comments = repo.findAllByApprovedAndEventIdAndSessionIdOrderByTimeDesc(status, eventId, sessionId);
            } else if (OLDEST.equals(type)) {
                comments = repo.findAllByApprovedAndEventIdAndSessionIdOrderByTimeAsc(status, eventId, sessionId);
            } else {
                comments = repo.findAllByApprovedAndEventIdAndSessionId(status, eventId, sessionId);
            }
        }

        return Objects.nonNull(comments) ? comments : new ArrayList<>();
    }

    public List<Comment> findAllByStatusAndEventId(String status, String eventId, int count, String type, int page) {
        List<Comment> comments;

        if (count != 0) {
            if (TOP_RATED.equals(type)) {
                comments = repo.findAllByApprovedAndEventIdOrderByRateDescPageable(status, eventId, page, count);
            } else if (RECENT.equals(type)) {
                comments = repo.findAllByApprovedAndEventIdOrderByTimeDescPageable(status, eventId, page, count);
            } else if (OLDEST.equals(type)) {
                comments = repo.findAllByApprovedAndEventIdOrderByTimeAscPageable(status, eventId, page, count);
            } else {
                comments = repo.findAllByApprovedAndEventIdPageable(status, eventId, page, count);
            }
        } else {
            if (TOP_RATED.equals(type)) {
                comments = repo.findAllByApprovedAndEventIdOrderByRateDesc(status, eventId);
            } else if (RECENT.equals(type)) {
                comments = repo.findAllByApprovedAndEventIdOrderByTimeDesc(status, eventId);
            } else if (OLDEST.equals(type)) {
                comments = repo.findAllByApprovedAndEventIdOrderByTimeAsc(status, eventId);
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