package javaday.istanbul.sliconf.micro.repository;

import javaday.istanbul.sliconf.micro.model.event.Comment;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.couchbase.repository.CouchbasePagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends CouchbasePagingAndSortingRepository<Comment, String> {

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

    /////////////////////////////////////////
    // Order By rate
    // eventId
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 ORDER BY rate DESC")
    List<Comment> findAllByApprovedAndEventIdOrderByRateDesc(String approved, String eventId);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 ORDER BY rate DESC OFFSET $3 LIMIT $4")
    List<Comment> findAllByApprovedAndEventIdOrderByRateDescPageable(String approve, String eventId, long offSet, long count);

    // eventId, sessionId
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 ORDER BY rate DESC")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdOrderByRateDesc(String approved, String eventId, String sessionId);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 ORDER BY rate DESC OFFSET $4 LIMIT $5")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdOrderByRateDescPageable(String approve, String eventId, String sessionId, long offSet, long count);

    // eventId, sessionId, userId
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4 ORDER BY rate DESC")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByRateDesc(String approved, String eventId, String sessionId, String userId);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4 ORDER BY rate DESC OFFSET $5 LIMIT $6")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByRateDescPageable(String approve, String eventId, String sessionId, String userId, long offSet, long count);

    // Count
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 LIMIT $3")
    List<Comment> findFirstByApprovedAndEventId(String approved, String eventId, int count);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 OFFSET $3 LIMIT $4")
    List<Comment> findAllByApprovedAndEventIdPageable(String approve, String eventId, long offSet, long count);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 LIMIT $4")
    List<Comment> findFirstByApprovedAndEventIdAndSessionId(String approved, String eventId, String sessionId, int count);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 OFFSET $4 LIMIT $5")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdPageable(String approve, String eventId, String sessionId, long offSet, long count);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4 LIMIT $5")
    List<Comment> findFirstByApprovedAndEventIdAndSessionIdAndUserId(String approved, String eventId, String sessionId, String userId, int count);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4 OFFSET $5 LIMIT $6")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdAndUserIdPageable(String approve, String eventId, String sessionId, String userId, long offSet, long count);

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

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 ORDER BY time DESC OFFSET $3 LIMIT $4")
    List<Comment> findAllByApprovedAndEventIdOrderByTimeDescPageable(String approve, String eventId, long offSet, long count);

    // eventId, sessionId
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 ORDER BY time DESC")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdOrderByTimeDesc(String approve, String eventId, String sessionId);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 ORDER BY time DESC OFFSET $4 LIMIT $5")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdOrderByTimeDescPageable(String approve, String eventId, String sessionId, long offSet, long count);

    // eventId, sessionId, userId
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4 ORDER BY time DESC")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByTimeDesc(String approve, String eventId, String sessionId, String userId);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4 ORDER BY time DESC OFFSET $5 LIMIT $6")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByTimeDescPageable(String approve, String eventId, String sessionId, String userId, long offSet, long count);

    ////////////////////////////
    // ORDER BY time OLDEST
    // eventId
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 ORDER BY time ASC")
    List<Comment> findAllByApprovedAndEventIdOrderByTimeAsc(String approve, String eventId);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 ORDER BY time ASC OFFSET $3 LIMIT $4")
    List<Comment> findAllByApprovedAndEventIdOrderByTimeAscPageable(String approve, String eventId, long offSet, long count);

    // eventId, sessionId
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 ORDER BY time ASC")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdOrderByTimeAsc(String approve, String eventId, String sessionId);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 ORDER BY time ASC OFFSET $4 LIMIT $5")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdOrderByTimeAscPageable(String approve, String eventId, String sessionId, long offSet, long count);

    //  eventId, sessionId, userId
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4 ORDER BY time ASC")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByTimeAsc(String approve, String eventId, String sessionId, String userId);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4 ORDER BY time ASC OFFSET $5 LIMIT $6")
    List<Comment> findAllByApprovedAndEventIdAndSessionIdAndUserIdOrderByTimeAscPageable(String approve, String eventId, String sessionId, String userId, long offSet, long count);

    ///////////////////////////////////////
    // Page Count
    @Query("#SELECT CEIL(count(*) / $3) FROM comments WHERE approved = $1 AND eventId = $2")
    int getPageCountForApprovedAndEventId(String approve, String eventId, long count);

    @Query("#SELECT CEIL(count(*) / $5) FROM comments WHERE approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4")
    int getPageCountForApprovedAndEventIdAndSessionIdAndUserId(String approve, String eventId, String sessionId, String userId, long count);

    @Query("#SELECT CEIL(count(*) / $4) FROM comments WHERE approved = $1 AND eventId = $2 AND sessionId = $3")
    int getPageCountForApprovedAndEventIdAndSessionId(String approve, String eventId, String sessionId, long count);

    ////////////////////////////////////////////////////////////////////
    @Query("#SELECT CEIL(count(*) / 10) FROM comments WHERE approved = $1 AND eventId = $2 AND sessionId = $3 AND userId = $4")
    int getPageCountForApprovedAndEventIdAndSessionIdAndUserId(String approve, String eventId, String sessionId, String userId);

    @Query("#SELECT COUNT(1) FROM comments WHERE 1")
    int getPageCountForApprovedAndEventIdAndSessionIdAndUserId();

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter}")
    Page<Comment> pageableTest(Pageable pageable);

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND commentValue = $1")
    Page<Comment> pageableTest(String commentValue, Pageable pageable);

    // MOST LIKED COMMENT
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND approved = $1 AND eventId = $2 ORDER BY rate DESC LIMIT 1")
    Comment findMostLikedComment(String status, String eventId);
}