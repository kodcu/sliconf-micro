package javaday.istanbul.sliconf.micro.security.token;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Sifre sifirlama sirasinda kullanilan token
 */
@Document(collection = "tokens")
@CompoundIndexes(
        @CompoundIndex(def = "{'id':1}")
)
@Getter
@Setter
public class Token {
    @Id
    private String id;
    private String tokenValue;
    private LocalDateTime validUntilDate;
    private String validMail;
    private String type;
}
