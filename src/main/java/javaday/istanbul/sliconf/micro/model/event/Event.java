package javaday.istanbul.sliconf.micro.model.event;


import com.couchbase.client.java.repository.annotation.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.couchbase.core.mapping.id.GenerationStrategy.UNIQUE;

public class Event {

    @Id
    @GeneratedValue(strategy = UNIQUE)
    private String id;

    @Field
    @NotNull
    private String key;

    @Field
    @NotNull
    private String name;

    @Field
    @NotNull
    private String logoPath;

    @Field
    @NotNull
    private LocalDateTime date;

    @Field
    @NotNull
    private String executiveUser;

    private List<AgendaElement> agenda;

    private List<Speaker> speakers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getExecutiveUser() {
        return executiveUser;
    }

    public void setExecutiveUser(String executiveUser) {
        this.executiveUser = executiveUser;
    }

    public List<AgendaElement> getAgenda() {
        return agenda;
    }

    public void setAgenda(List<AgendaElement> agenda) {
        this.agenda = agenda;
    }

    public List<Speaker> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(List<Speaker> speakers) {
        this.speakers = speakers;
    }
}
