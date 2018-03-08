package javaday.istanbul.sliconf.micro.model.token;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(collection = "tokens")
@CompoundIndexes(
        @CompoundIndex(def = "{'id':1}")
)
public class Token {
    @Id
    private String id;
    private String tokenValue;
    private LocalDateTime validUntilDate;
    private String validMail;
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
