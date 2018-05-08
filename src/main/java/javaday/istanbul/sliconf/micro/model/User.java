package javaday.istanbul.sliconf.micro.model;

import javaday.istanbul.sliconf.micro.util.Constants;
import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
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
        this.setToken(user.getToken());
    }

}
