package javaday.istanbul.sliconf.micro.model.event;


import javaday.istanbul.sliconf.micro.model.event.agenda.AgendaElement;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.springframework.data.couchbase.core.mapping.id.GenerationStrategy.UNIQUE;

@Document
public class Event {

    @Id
    @GeneratedValue(strategy = UNIQUE)
    private String id;

    private String key;

    private String name;

    private String logoPath;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String executiveUser;

    private List<AgendaElement> agenda;

    private List<Speaker> speakers;

    private boolean status;

    private String description;

    private About about;

    private List<Room> rooms;

    private List<Floor> floorPlan;

    private Map<String, String> sponsorTags;

    private Map<String, List<Sponsor>> sponsors;

    private Boolean deleted = false;

    private StatusDetails statusDetails;

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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Floor> getFloorPlan() {
        return floorPlan;
    }

    public void setFloorPlan(List<Floor> floorPlan) {
        this.floorPlan = floorPlan;
    }

    public Map<String, String> getSponsorTags() {
        return sponsorTags;
    }

    public void setSponsorTags(Map<String, String> sponsorTags) {
        this.sponsorTags = sponsorTags;
    }

    public Map<String, List<Sponsor>> getSponsors() {
        return sponsors;
    }

    public void setSponsors(Map<String, List<Sponsor>> sponsors) {
        this.sponsors = sponsors;
    }

    public About getAbout() {
        return about;
    }

    public void setAbout(About about) {
        this.about = about;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public StatusDetails getStatusDetails() {
        return statusDetails;
    }

    public void setStatusDetails(StatusDetails statusDetails) {
        this.statusDetails = statusDetails;
    }
}
