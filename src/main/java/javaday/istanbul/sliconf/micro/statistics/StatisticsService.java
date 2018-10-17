package javaday.istanbul.sliconf.micro.statistics;

import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.event.Speaker;
import javaday.istanbul.sliconf.micro.model.event.agenda.AgendaElement;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.survey.service.GeneralService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class StatisticsService {

    private final EventRepositoryService eventRepositoryService;

    private final GeneralService generalService;


    ResponseMessage getEventSessionsStatistics(String eventKey) {

        Event event = (Event) generalService.findEventByIdOrEventKey(eventKey).getReturnObject();
        List<AgendaElement> agendaElements = new ArrayList<>(event.getAgenda());
        List<Speaker> speakers = new ArrayList<>(event.getSpeakers());
        List<EventSessionStatisticsDTO> eventSessionStatisticsDTOList = new ArrayList<>();

        agendaElements.forEach(agendaElement -> speakers.forEach(speaker -> {
            if(agendaElement.getSpeaker().equals(speaker.getId())) {
                EventSessionStatisticsDTO eventSessionStatisticsDTO = new EventSessionStatisticsDTO();
                eventSessionStatisticsDTO.setPhoto(speaker.getProfilePicture());
                String average = String.valueOf(agendaElement.getStar()) ;
                eventSessionStatisticsDTO.setAverage(average);
                eventSessionStatisticsDTO.setCount(String.valueOf(agendaElement.getVoteCount()));
                eventSessionStatisticsDTO.setTopic(agendaElement.getTopic());
                eventSessionStatisticsDTO.setWorkingAt(speaker.getWorkingAt());
                eventSessionStatisticsDTO.setSpeaker(speaker.getName());
                eventSessionStatisticsDTOList.add(eventSessionStatisticsDTO);
            }
        }));

        return new ResponseMessage(true, "Session Statistics Listed", eventSessionStatisticsDTOList);

    }
}
