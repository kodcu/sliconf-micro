package javaday.istanbul.sliconf.micro.model;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonProperty;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.couchbase.client.java.repository.annotation.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.data.couchbase.core.mapping.id.GenerationStrategy.UNIQUE;

/**
 * Created by ttayfur on 7/6/17.
 */
@Document
public class User {
    @Id
    @GeneratedValue(strategy = UNIQUE)
    private String id;

    @Field
    @NotNull
    private String name;

    @Field
    @NotNull
    private String email;

    @Field
    @NotNull
    private String password;

    @Field
    @NotNull
    @JsonProperty("hashedPassword")
    private byte[] hashedPassword;

    @Field
    @NotNull
    @JsonProperty("salt")
    private byte[] salt;

    public User() {
    }

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

    @JsonSerialize(using = javaday.istanbul.sliconf.micro.serializator.ByteArraySerializer.class)
    public byte[] getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(byte[] hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword.getBytes();
    }

    @JsonSerialize(using = javaday.istanbul.sliconf.micro.serializator.ByteArraySerializer.class)
    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public void setSalt(String salt) {
        this.salt = salt.getBytes();
    }

    public void generateId() {
        this.id = this.name + "-" + this.email + "-" + UUID.randomUUID().toString();
    }
}
