package javaday.istanbul.sliconf.micro.model.event;

/**
 * Oylarda gozuken user
 */
public class VoteUser {
    private String userId;
    private String username;
    private String fullname;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}
