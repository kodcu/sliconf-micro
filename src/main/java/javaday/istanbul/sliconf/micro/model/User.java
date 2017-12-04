package javaday.istanbul.sliconf.micro.model;

import com.couchbase.client.java.repository.annotation.Field;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;

import static org.springframework.data.couchbase.core.mapping.id.GenerationStrategy.UNIQUE;

/**
 * Created by ttayfur on 7/6/17.
 */
@Document
public class User {

    @Id
    @GeneratedValue(strategy = UNIQUE)
    @Field
    private String id;

    @Field
    private String username;

    @Field
    @Value("${sliconf.user.fullname}")
    private String fullname;

    @Field
    private String email;

    @Field
    private String password;

    @Field
    private byte[] hashedPassword;

    @Field
    private byte[] salt;


    public User() {
        // for instantiation
    }

    /**
     * Clones user
     *
     * @param user
     */
    public User(User user) {
        this.setUsername(user.getUsername());
        this.setPassword(user.getPassword());
        this.setEmail(user.getEmail());
        this.setId(user.getId());
        this.setSalt(user.getSalt());
        this.setHashedPassword(user.getHashedPassword());
        this.setFullname(user.getFullname());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
