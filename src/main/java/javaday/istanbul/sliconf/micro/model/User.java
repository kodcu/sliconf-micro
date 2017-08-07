package javaday.istanbul.sliconf.micro.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Created by ttayfur on 7/6/17.
 */
public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private byte[] hashedPassword;
    private byte[] salt;

    public User(String id, String name, String email, String password) {
        if (Objects.isNull(id)) {
            this.id = UUID.randomUUID().toString();
        } else {
            this.id = id;
        }

        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(byte[] hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public void generateId() {
        this.id = this.name + "-" + this.email + "-" + UUID.randomUUID().toString();
    }
}
