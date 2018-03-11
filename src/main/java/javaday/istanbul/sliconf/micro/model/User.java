package javaday.istanbul.sliconf.micro.model;

import javaday.istanbul.sliconf.micro.util.Constants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by ttayfur on 7/6/17.
 */
@Document(collection = "users")
@CompoundIndexes(
        @CompoundIndex(def = "{'id':1}")
)
public class User implements Serializable {
    @Id
    private String id;
    private String username;
    private String fullname;
    private String email;
    private String password;
    private byte[] hashedPassword;
    private byte[] salt;
    private Boolean anonymous;
    private String deviceId;
    private String role;
    private String token;


    public User() {
        // for instantiation
        if (Objects.isNull(fullname) || fullname.isEmpty()) {
            this.fullname = Constants.DEFAULT_USER_FULLNAME;
        }

        if (Objects.isNull(role) || role.isEmpty()) {
            this.role = Constants.DEFAULT_USER_ROLE;
        }
    }

    /**
     * Clones user
     *
     * @param user kopyalanacak olan user
     */
    public User(User user) {
        this.setUsername(user.getUsername());
        this.setPassword(user.getPassword());
        this.setEmail(user.getEmail());
        this.setId(user.getId());
        this.setSalt(user.getSalt());
        this.setHashedPassword(user.getHashedPassword());
        this.setFullname(user.getFullname());
        this.setAnonymous(user.getAnonymous());
        this.setDeviceId(user.getDeviceId());
        this.setRole(user.getRole());
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

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
