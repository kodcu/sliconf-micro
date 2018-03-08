package javaday.istanbul.sliconf.micro.model.event;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;


@Document(collection = "comments")
@CompoundIndexes(
        @CompoundIndex(def = "{'id':1}")
)
public class Comment {
    @Id
    private String id;
    private String eventId;
    private String sessionId;
    private String userId;
    private LocalDateTime time;
    private int like;
    private int dislike;
    private List<VoteUser> likes;
    private List<VoteUser> dislikes;
    private String commentValue;

    private String approved;
    private String commentType;

    private String username;
    private String fullname;
    private String roomName;
    private String topic;

    private int rate;

    private Boolean anonymous;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }

    public List<VoteUser> getLikes() {
        return likes;
    }

    public void setLikes(List<VoteUser> likes) {
        this.likes = likes;
    }

    public List<VoteUser> getDislikes() {
        return dislikes;
    }

    public void setDislikes(List<VoteUser> dislikes) {
        this.dislikes = dislikes;
    }

    public String getCommentValue() {
        return commentValue;
    }

    public void setCommentValue(String commentValue) {
        this.commentValue = commentValue;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }
}
