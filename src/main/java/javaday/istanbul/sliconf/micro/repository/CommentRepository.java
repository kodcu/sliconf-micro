package javaday.istanbul.sliconf.micro.repository;

import javaday.istanbul.sliconf.micro.model.event.Comment;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends CrudRepository<Comment, String> {

    List<Comment> findAllByEventIdAndSessionIdAndUserIdAndApproved(String eventId, String sessionId, String userId, String approved);

    List<Comment> findAllByEventIdAndSessionIdAndUserId(String eventId, String sessionId, String userId);

    List<Comment> findAllByEventIdAndSessionId(String eventId, String sessionId);

    List<Comment> findAllByEventId(String eventId);


    List<Comment> findAllByApprovedAndEventId(String approved, String eventId);
    List<Comment> findAllByApprovedAndEventIdAndSessionId(String approved, String eventId, String sessionId);
    List<Comment> findAllByApprovedAndEventIdAndSessionIdAndUserId(String approved, String eventId, String sessionId, String userId);


    // Count and Order By rate
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 ORDER BY rate DESC LIMIT $3")
    List<Comment> findFirstByApprovedAndEventIdOrderByRateDesc(String approved, String eventId, int count);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 ORDER BY rate DESC LIMIT $4")
    List<Comment> findFirstByApprovedAndEventIdAndSessionIdOrderByRateDesc(String approved, String eventId, String sessionId, int count);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4 ORDER BY rate DESC LIMIT $5")
    List<Comment> findFirstByApprovedAndEventIdAndSessionIdAndUserIdOrderByRateDesc(String approved, String eventId, String sessionId, String userId, int count);

    // Order By rate
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 ORDER BY rate DESC")
    List<Comment> findAllByApprovedAndEventIdOrderByRateDesc(String approved, String eventId);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 ORDER BY rate DESC")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdOrderByRateDesc(String approved, String eventId, String sessionId);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4 ORDER BY rate DESC")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByRateDesc(String approved, String eventId, String sessionId, String userId);

    // Count
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 LIMIT $3")
    List<Comment> findFirstByApprovedAndEventId(String approved, String eventId, int count);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 LIMIT $4")
    List<Comment> findFirstByApprovedAndEventIdAndSessionId(String approved, String eventId, String sessionId, int count);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4 LIMIT $5")
    List<Comment> findFirstByApprovedAndEventIdAndSessionIdAndUserId(String approved, String eventId, String sessionId, String userId, int count);

    // Count ORDER BY time RECENT
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 ORDER BY time DESC LIMIT $3")
    List<Comment> findFirstByApprovedAndEventIdOrderByTimeDesc(String approve, String eventId, int count);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 ORDER BY time DESC LIMIT $4")
    List<Comment> findFirstByApprovedAndEventIdAndSessionIdOrderByTimeDesc(String approve, String eventId, String sessionId, int count);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4 ORDER BY time DESC LIMIT $5")
    List<Comment> findFirstByApprovedAndEventIdAndSessionIdAndUserIdOrderByTimeDesc(String approve, String eventId, String sessionId, String userId, int count);

    // ORDER BY time RECENT
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 ORDER BY time DESC")
    List<Comment> findAllByApprovedAndEventIdOrderByTimeDesc(String approve, String eventId);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 ORDER BY time DESC")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdOrderByTimeDesc(String approve, String eventId, String sessionId);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4 ORDER BY time DESC")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByTimeDesc(String approve, String eventId, String sessionId, String userId);
}