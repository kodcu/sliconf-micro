package javaday.istanbul.sliconf.micro.survey.service;

import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.event.agenda.AgendaElement;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.CommentMessageProvider;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.survey.SurveyException;
import javaday.istanbul.sliconf.micro.survey.SurveyMessageProvider;
import javaday.istanbul.sliconf.micro.survey.SurveyRepository;
import javaday.istanbul.sliconf.micro.survey.model.Survey;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
@Service
public class GeneralService {

    private final SurveyMessageProvider surveyMessageProvider;
    private final SurveyRepository surveyRepository;
    private final EventRepositoryService eventRepositoryService;
    private final UserRepositoryService userRepositoryService;
    private final CommentMessageProvider commentMessageProvider;


    ResponseMessage findSurveyById(String surveyId) {

        ResponseMessage responseMessage = new ResponseMessage();
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> {
            log.error("survey not found by id: {}", surveyId);
            String message = surveyMessageProvider.getMessage("surveyCanNotFoundWithGivenId");
            return new SurveyException(message, surveyId);
        });
        responseMessage.setStatus(true);
        responseMessage.setReturnObject(survey);
        return responseMessage;
    }

    ResponseMessage findEventByKey(String eventKey) {
        ResponseMessage responseMessage = new ResponseMessage();
        Event event = (Event) eventRepositoryService.findByKey(eventKey).orElseThrow(() -> {
            log.error("event not found by id: {}", eventKey);
            String message = surveyMessageProvider.getMessage("eventCanNotFoundWithGivenId");
            return new SurveyException(message, eventKey);
        });
        responseMessage.setStatus(true);
        responseMessage.setReturnObject(event);
        return responseMessage;
    }

    ResponseMessage findUserById(String userId) {
        ResponseMessage responseMessage = new ResponseMessage();
        User user = userRepositoryService.findById(userId).orElseThrow(() -> {
            log.error("User not found by id: {}", userId);
            String message = surveyMessageProvider.getMessage("userCanNotFoundWithGivenId");
            return new SurveyException(message, userId);
        });
        responseMessage.setStatus(true);
        responseMessage.setReturnObject(user);
        return responseMessage;
    }

    ResponseMessage getAgendaElement(List<AgendaElement> agendaElements, String agendaElementId) {
        if (Objects.nonNull(agendaElements)) {
            AgendaElement agendaElementReturn = agendaElements
                    .stream()
                    .filter(agendaElement -> Objects.nonNull(agendaElement) &&
                            Objects.nonNull(agendaElement.getId()) &&
                            agendaElement.getId().equals(agendaElementId)).findFirst().orElse(null);

            if (Objects.nonNull(agendaElementReturn)) {
                return new ResponseMessage(true, commentMessageProvider.getMessage("agendaElementFoundWithGivenId"), agendaElementReturn);
            }
        }

        return new ResponseMessage(false, commentMessageProvider.getMessage("agendaElementCanNotFoundWithGivenId"), agendaElementId);
    }
}
