package javaday.istanbul.sliconf.micro.model.event;


import java.util.List;

public class Event extends CoreEvent{
    private List<AgendaElement> agenda;
    private List<Speaker> speakers;

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
