package javaday.istanbul.sliconf.micro.statistics;

import javaday.istanbul.sliconf.micro.agenda.model.AgendaElement;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.speaker.Speaker;
import javaday.istanbul.sliconf.micro.survey.service.GeneralService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class StatisticsService {

    private final GeneralService generalService;

    public ResponseMessage getEventSessionsStatistics(String eventKey) {

        Event event = (Event) generalService.findEventByIdOrEventKey(eventKey).getReturnObject();
        List<AgendaElement> agendaElements = new ArrayList<>(event.getAgenda());
        List<Speaker> speakers = new ArrayList<>(event.getSpeakers());
        List<EventSessionStatisticsDTO> eventSessionStatisticsDTOList = new ArrayList<>();

        agendaElements.forEach(agendaElement -> speakers.forEach(speaker -> {

            if (agendaElement.getSpeaker().equals(speaker.getId())) {

                EventSessionStatisticsDTO eventSessionStatisticsDTO = new EventSessionStatisticsDTO();
                eventSessionStatisticsDTO.setPhoto(speaker.getProfilePicture());
                String average = String.valueOf(agendaElement.getStar());
                eventSessionStatisticsDTO.setAverage(average);
                eventSessionStatisticsDTO.setVoteCount(String.valueOf(agendaElement.getVoteCount()));
                eventSessionStatisticsDTO.setTopic(agendaElement.getTopic());
                eventSessionStatisticsDTO.setWorkingAt(speaker.getWorkingAt());
                eventSessionStatisticsDTO.setSpeaker(speaker.getName());
                eventSessionStatisticsDTOList.add(eventSessionStatisticsDTO);
            }
        }));


        return new ResponseMessage(true, "Session Statistics Listed", eventSessionStatisticsDTOList);

    }
}
