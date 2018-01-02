package javaday.istanbul.sliconf.micro.model;

import java.util.List;

public class ModerateCommentModel {
    private String eventId;
    private String userId;
    private List<String> approved;
    private List<String> denied;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getApproved() {
        return approved;
    }

    public void setApproved(List<String> approved) {
        this.approved = approved;
    }

    public List<String> getDenied() {
        return denied;
    }

    public void setDenied(List<String> denied) {
        this.denied = denied;
    }
}
