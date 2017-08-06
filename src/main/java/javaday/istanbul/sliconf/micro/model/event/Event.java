package javaday.istanbul.sliconf.micro.model.event;


import java.util.List;

public class Event extends CoreEvent{
    private List<AgendaElement> agenda;
    private List<Speaker> speakers;
}
