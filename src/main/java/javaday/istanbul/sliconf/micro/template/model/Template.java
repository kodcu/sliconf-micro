package javaday.istanbul.sliconf.micro.template.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * admine gonderilen mail body template nesnesi
 */
@Document(collection = "templates")
@CompoundIndexes(
        @CompoundIndex(def = "{'id':1,'code':1}", unique = true)
)
@Getter
@Setter
public class Template {
    @Id
    private String id;
    @Indexed
    private   String title;
    private  String content;
    private String code;
}
