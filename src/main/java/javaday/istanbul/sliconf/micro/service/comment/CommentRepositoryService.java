package javaday.istanbul.sliconf.micro.service.comment;

import javaday.istanbul.sliconf.micro.model.event.Comment;
import javaday.istanbul.sliconf.micro.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentRepositoryService implements CommentService {

    @Autowired
    private CommentRepository repo;

    private static final String TOP_RATED = "top-rated";
    private static final String RECENT = "recent";
    private static final String OLDEST = "oldest";

    private Sort sortRate = new Sort(Sort.Direction.DESC, "rate");
    private Sort sortTimeDESC = new Sort(Sort.Direction.DESC, "time");
    private Sort sortTimeASC = new Sort(Sort.Direction.ASC, "time");

    public Comment save(Comment comment) {
        return repo.save(comment);
    }

    public List<Comment> findAllByStatusAndEventIdAndSessionIdAndUserId(String status, String eventId, String sessionId, String userId, int count, String type, int page) {
        List<Comment> comments = null;
        Page<Comment> pages = null;

        if (count != 0) {

            Pageable ratePageable = new PageRequest(page, count, sortRate);
            Pageable timePageableDESC = new PageRequest(page, count, sortTimeDESC);
            Pageable timePageableASC = new PageRequest(page, count, sortTimeASC);

            Pageable pageable = new PageRequest(page, count);

            if (TOP_RATED.equals(type)) {
                pages = repo.findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByRateDesc(status, eventId, sessionId, userId, ratePageable);
            } else if (RECENT.equals(type)) {
                pages = repo.findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByTimeDesc(status, eventId, sessionId, userId, timePageableDESC);
            } else if (OLDEST.equals(type)) {
                pages = repo.findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByTimeAsc(status, eventId, sessionId, userId, timePageableASC);
            } else {
                pages = repo.findAllByApprovedAndEventIdAndSessionIdAndUserId(status, eventId, sessionId, userId, pageable);
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

        if (Objects.nonNull(pages) && Objects.nonNull(pages.getContent())) {
            return pages.getContent();
        }

        return Objects.nonNull(comments) ? comments : new ArrayList<>();
    }

    public List<Comment> findAllByStatusAndEventIdAndSessionId(String status, String eventId, String sessionId, int count, String type, int page) {
        List<Comment> comments = null;
        Page<Comment> pages = null;

        if (count != 0) {

            Pageable ratePageable = new PageRequest(page, count, sortRate);
            Pageable timePageableDESC = new PageRequest(page, count, sortTimeDESC);
            Pageable timePageableASC = new PageRequest(page, count, sortTimeASC);

            Pageable pageable = new PageRequest(page, count);

            if (TOP_RATED.equals(type)) {
                pages = repo.findAllByApprovedAndEventIdAndSessionIdOrderByRateDesc(status, eventId, sessionId, ratePageable);
            } else if (RECENT.equals(type)) {
                pages = repo.findAllByApprovedAndEventIdAndSessionIdOrderByTimeDesc(status, eventId, sessionId, timePageableDESC);
            } else if (OLDEST.equals(type)) {
                pages = repo.findAllByApprovedAndEventIdAndSessionIdOrderByTimeAsc(status, eventId, sessionId, timePageableASC);
            } else {
                pages = repo.findAllByApprovedAndEventIdAndSessionId(status, eventId, sessionId, pageable);
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

        if (Objects.nonNull(pages) && Objects.nonNull(pages.getContent())) {
            return pages.getContent();
        }

        return Objects.nonNull(comments) ? comments : new ArrayList<>();
    }

    public List<Comment> findAllByStatusAndEventId(String status, String eventId, int count, String type, int page) {
        List<Comment> comments = null;
        Page<Comment> pages = null;

        if (count != 0) {

            Pageable ratePageable = new PageRequest(page, count, sortRate);
            Pageable timePageableDESC = new PageRequest(page, count, sortTimeDESC);
            Pageable timePageableASC = new PageRequest(page, count, sortTimeASC);

            Pageable pageable = new PageRequest(page, count);

            if (TOP_RATED.equals(type)) {
                pages = repo.findAllByApprovedAndEventIdOrderByRateDesc(status, eventId, ratePageable);
            } else if (RECENT.equals(type)) {
                pages = repo.findAllByApprovedAndEventIdOrderByTimeDesc(status, eventId, timePageableDESC);
            } else if (OLDEST.equals(type)) {
                pages = repo.findAllByApprovedAndEventIdOrderByTimeAsc(status, eventId, timePageableASC);
            } else {
                pages = repo.findAllByApprovedAndEventId(status, eventId, pageable);
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

        if (Objects.nonNull(pages) && Objects.nonNull(pages.getContent())) {
            return pages.getContent();
        }

        return Objects.nonNull(comments) ? comments : new ArrayList<>();
    }

    public Comment findMostLikedComment(String status, String eventId) {
        Pageable ratePageable = new PageRequest(0, 1, sortRate);
        Page<Comment> commentPage = repo.findByApprovedAndEventId(status, eventId, ratePageable);

        List<Comment> comments = Objects.nonNull(commentPage) ? commentPage.getContent() : new ArrayList<>();

        Comment comment = Objects.nonNull(comments) && !comments.isEmpty() ? comments.get(0) : new Comment();

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

    public List<Comment> findAll() {
        return repo.findAll();
    }
}