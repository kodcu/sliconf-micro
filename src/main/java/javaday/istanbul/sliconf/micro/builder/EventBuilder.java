package javaday.istanbul.sliconf.micro.builder;

import javaday.istanbul.sliconf.micro.model.event.Event;

import java.time.LocalDateTime;

/**
 * Event nesnesini olustururken builder yapisi icin kullanilan sinif
 */
public class EventBuilder {

    private Event event;

    public EventBuilder() {
        this.event = new Event();
    }

    public EventBuilder setName(String name) {
        this.event.setName(name);
        return this;
    }

    public EventBuilder setId(String id) {
        this.event.setId(id);
        return this;
    }

    public EventBuilder setKey(String key) {
        this.event.setKey(key);
        return this;
    }

    public EventBuilder setDate(LocalDateTime date) {
        this.event.setStartDate(date);
        return this;
    }

    public EventBuilder setEndDate(LocalDateTime date) {
        this.event.setEndDate(date);
        return this;
    }

    public EventBuilder setLogoPath(String logoPath) {
        this.event.setLogoPath(logoPath);
        return this;
    }

    /**
     * Kullanicinin idsi
     *
     * @param executiveUser
     * @return
     */
    public EventBuilder setExecutiveUser(String executiveUser) {
        this.event.setExecutiveUser(executiveUser);
        return this;
    }

    public Event build() {
        return this.event;
    }
}
