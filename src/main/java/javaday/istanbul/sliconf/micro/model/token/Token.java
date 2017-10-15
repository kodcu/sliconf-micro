package javaday.istanbul.sliconf.micro.model.token;

import com.couchbase.client.java.repository.annotation.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;

import java.time.LocalDateTime;

import static org.springframework.data.couchbase.core.mapping.id.GenerationStrategy.UNIQUE;

@Document
public class Token {

    @Id
    @GeneratedValue(strategy = UNIQUE)
    private String id;

    @Field
    private String tokenValue;

    @Field
    private LocalDateTime validUntilDate;

    @Field
    private String validMail;

    @Field
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public LocalDateTime getValidUntilDate() {
        return validUntilDate;
    }

    public void setValidUntilDate(LocalDateTime validUntilDate) {
        this.validUntilDate = validUntilDate;
    }

    public String getValidMail() {
        return validMail;
    }

    public void setValidMail(String validMail) {
        this.validMail = validMail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
