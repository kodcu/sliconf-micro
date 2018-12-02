package javaday.istanbul.sliconf.micro.admin;

import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.event.model.LifeCycleState;
import javaday.istanbul.sliconf.micro.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Component
public class AdminMailService {

    private final EventRepository eventRepository;

    @Value("${spring.profiles.active}")
    private String activeProfile;
    public Set<Event> getEvents(){
        List<LifeCycleState.EventStatus> eventStatuses = new ArrayList<>();

        eventStatuses.add(LifeCycleState.EventStatus.ACTIVE);

        Set<Event> events = new HashSet<>();
        if(!activeProfile.equals("test"))
            for (LifeCycleState.EventStatus eventStatus : eventStatuses) {
                events.addAll(eventRepository.findAllByLifeCycleStateEventStatusesLike(eventStatus));
            }
            return events;

    }

    public String getThisWeek(){
        StringBuilder thisWeek=new StringBuilder();
        Set<Event> events =getEvents();

        LocalDateTime today7 =  LocalDateTime.now().plusDays(7);//thisweekvariable
        LocalDateTime now=LocalDateTime.now();//now
        for (Event event:events) {
            if (event.getStartDate().compareTo(today7) <= 0&& event.getStartDate().compareTo(now)>0) {
                thisWeek.append("<li>"+event.getName() + "</li>");        //get events of this week
            }
        }
        if  (thisWeek.toString().isEmpty())
        {
            thisWeek.append("<li>No events to display for now </li>");           //when is not event
        }
        return thisWeek.toString();
    }
    public String getNextWeek(){
        StringBuilder nextWeek=new StringBuilder();
        Set<Event> events = getEvents();
        LocalDateTime today7 =  LocalDateTime.now().plusDays(7);      //thisweekvariable
        LocalDateTime today14 = LocalDateTime.now().plusDays(14);      //nextweekvariable
        for (Event event:events) {
            if(event.getStartDate().compareTo(today14) <= 0 && event.getStartDate().compareTo(today7) > 0 )
            {
                nextWeek.append("<li>"+event.getName() + "</li>") ;         //get events of next week
            }
        }
        if  (nextWeek.toString().isEmpty())
        {
            nextWeek.append("<li>No events to display for now </li>");           //when is not event
        }
        return nextWeek.toString();

    }

}