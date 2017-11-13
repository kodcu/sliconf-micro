package javaday.istanbul.sliconf.micro.model.event;

import java.time.LocalDateTime;
import java.util.List;

public class AgendaElement {
    private String id;
    private String time;
    private String topic;
    private String topicDetail;
    private int level;
    private List<String> tags;
    private String room;
    private String speaker;
    private double star;
    private LocalDateTime date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopicDetail() {
        return topicDetail;
    }

    public void setTopicDetail(String topicDetail) {
        this.topicDetail = topicDetail;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public double getStar() {
        return star;
    }

    public void setStar(double star) {
        this.star = star;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
