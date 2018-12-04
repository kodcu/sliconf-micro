package javaday.istanbul.sliconf.micro.user.util;

import javaday.istanbul.sliconf.micro.user.model.User;

/**
 * User nesnesini olustururken builder yapisi icin kullanilan sinif
 */
public class UserBuilder {

    private User user;

    public UserBuilder() {
        this.user = new User();
    }

    public UserBuilder setName(String name) {
        this.user.setUsername(name);
        return this;
    }

    public UserBuilder setFullName(String fullName) {
        this.user.setFullName(fullName);
        return this;
    }

    public UserBuilder setEmail(String email) {
        this.user.setEmail(email);
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.user.setPassword(password);
        return this;
    }

    public UserBuilder setUserName(String userName) {
        this.user.setUsername(userName);
        return this;
    }


    public User build() {
        return this.user;
    }
}
