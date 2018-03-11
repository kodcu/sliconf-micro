package javaday.istanbul.sliconf.micro.repository;

import javaday.istanbul.sliconf.micro.model.event.Comment;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends MongoRepository<Comment, String>, CrudRepository<Comment, String> {

    List<Comment> findAllByEventIdAndSessionIdAndUserIdAndApproved(String eventId, String sessionId, String userId, String approved);

    List<Comment> findAllByEventIdAndSessionIdAndUserId(String eventId, String sessionId, String userId);

    List<Comment> findAllByEventIdAndSessionId(String eventId, String sessionId);

    List<Comment> findAllByEventId(String eventId);

    List<Comment> findAllByApprovedAndEventId(String approved, String eventId);

    List<Comment> findAllByApprovedAndEventIdAndSessionId(String approved, String eventId, String sessionId);

    List<Comment> findAllByApprovedAndEventIdAndSessionIdAndUserId(String approved, String eventId, String sessionId, String userId);

    /////////////////////////////////////////
    // Order By rate
    // eventId
    //@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 ORDER BY rate DESC")
    List<Comment> findAllByApprovedAndEventIdOrderByRateDesc(String approved, String eventId);

    //@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 ORDER BY rate DESC OFFSET $3 LIMIT $4")
    Page<Comment> findAllByApprovedAndEventIdOrderByRateDesc(String approve, String eventId, Pageable pageable);

    // eventId, sessionId
    //@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 ORDER BY rate DESC")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdOrderByRateDesc(String approved, String eventId, String sessionId);

    //@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 ORDER BY rate DESC OFFSET $4 LIMIT $5")
    Page<Comment> findAllByApprovedAndEventIdAndSessionIdOrderByRateDesc(String approve, String eventId, String sessionId, Pageable pageable);

    // eventId, sessionId, userId
    //@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4 ORDER BY rate DESC")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByRateDesc(String approved, String eventId, String sessionId, String userId);

    //@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4 ORDER BY rate DESC OFFSET $5 LIMIT $6")
    Page<Comment> findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByRateDesc(String approve, String eventId, String sessionId, String userId, Pageable pageable);

    // Count

    //@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 OFFSET $3 LIMIT $4")
    Page<Comment> findAllByApprovedAndEventId(String approve, String eventId, Pageable pageable);

    //@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 OFFSET $4 LIMIT $5")
    Page<Comment> findAllByApprovedAndEventIdAndSessionId(String approve, String eventId, String sessionId, Pageable pageable);

    //@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4 OFFSET $5 LIMIT $6")
    Page<Comment> findAllByApprovedAndEventIdAndSessionIdAndUserId(String approve, String eventId, String sessionId, String userId, Pageable pageable);

    // Count ORDER BY time RECENT
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 ORDER BY time DESC LIMIT $3")
    List<Comment> findFirstByApprovedAndEventIdOrderByTimeDesc(String approve, String eventId, int count);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 ORDER BY time DESC LIMIT $4")
    List<Comment> findFirstByApprovedAndEventIdAndSessionIdOrderByTimeDesc(String approve, String eventId, String sessionId, int count);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4 ORDER BY time DESC LIMIT $5")
    List<Comment> findFirstByApprovedAndEventIdAndSessionIdAndUserIdOrderByTimeDesc(String approve, String eventId, String sessionId, String userId, int count);

    // Count ORDER BY time OLDEST
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 ORDER BY time ASC LIMIT $3")
    List<Comment> findFirstByApprovedAndEventIdOrderByTimeAsc(String approve, String eventId, int count);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 ORDER BY time ASC LIMIT $4")
    List<Comment> findFirstByApprovedAndEventIdAndSessionIdOrderByTimeAsc(String approve, String eventId, String sessionId, int count);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4 ORDER BY time ASC LIMIT $5")
    List<Comment> findFirstByApprovedAndEventIdAndSessionIdAndUserIdOrderByTimeAsc(String approve, String eventId, String sessionId, String userId, int count);


    ////////////////////////////////
    // ORDER BY time RECENT
    // eventId
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 ORDER BY time DESC")
    List<Comment> findAllByApprovedAndEventIdOrderByTimeDesc(String approve, String eventId);

    //@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 ORDER BY time DESC OFFSET $3 LIMIT $4")
    Page<Comment> findAllByApprovedAndEventIdOrderByTimeDesc(String approve, String eventId, Pageable pageable);

    // eventId, sessionId
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 ORDER BY time DESC")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdOrderByTimeDesc(String approve, String eventId, String sessionId);

    //@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 ORDER BY time DESC OFFSET $4 LIMIT $5")
    Page<Comment> findAllByApprovedAndEventIdAndSessionIdOrderByTimeDesc(String approve, String eventId, String sessionId, Pageable pageable);

    // eventId, sessionId, userId
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4 ORDER BY time DESC")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByTimeDesc(String approve, String eventId, String sessionId, String userId);

    //@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4 ORDER BY time DESC OFFSET $5 LIMIT $6")
    Page<Comment> findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByTimeDesc(String approve, String eventId, String sessionId, String userId, Pageable pageable);

    ////////////////////////////
    // ORDER BY time OLDEST
    // eventId
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 ORDER BY time ASC")
    List<Comment> findAllByApprovedAndEventIdOrderByTimeAsc(String approve, String eventId);

    //@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 ORDER BY time ASC OFFSET $3 LIMIT $4")
    Page<Comment> findAllByApprovedAndEventIdOrderByTimeAsc(String approve, String eventId, Pageable pageable);

    // eventId, sessionId
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 ORDER BY time ASC")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdOrderByTimeAsc(String approve, String eventId, String sessionId);

    //@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 ORDER BY time ASC OFFSET $4 LIMIT $5")
    Page<Comment> findAllByApprovedAndEventIdAndSessionIdOrderByTimeAsc(String approve, String eventId, String sessionId, Pageable pageable);

    //  eventId, sessionId, userId
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4 ORDER BY time ASC")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByTimeAsc(String approve, String eventId, String sessionId, String userId);

    //@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4 ORDER BY time ASC OFFSET $5 LIMIT $6")
    Page<Comment> findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByTimeAsc(String approve, String eventId, String sessionId, String userId, Pageable pageable);

    // MOST LIKED COMMENT
    //@Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 ORDER BY rate DESC LIMIT 1")
    Comment findTopByRateAndApprovedAndEventId(String status, String eventId);
}