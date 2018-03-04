package javaday.istanbul.sliconf.micro.model.token;

import javaday.istanbul.sliconf.micro.model.User;

import java.io.Serializable;
import java.util.Date;

public class SecurityToken implements Serializable {

    private String username;
    private String role;
    private User user;
    private Date validUntilDate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getValidUntilDate() {
        return validUntilDate;
    }

    public void setValidUntilDate(Date validUntilDate) {
        this.validUntilDate = validUntilDate;
    }
}
