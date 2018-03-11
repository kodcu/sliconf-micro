package javaday.istanbul.sliconf.micro.model.event;

import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
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
}
