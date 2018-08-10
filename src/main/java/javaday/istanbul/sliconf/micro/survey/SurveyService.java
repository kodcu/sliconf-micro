package javaday.istanbul.sliconf.micro.survey;

import javaday.istanbul.sliconf.micro.exceptions.EventNotFoundException;
import javaday.istanbul.sliconf.micro.exceptions.UserNotFoundException;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.event.agenda.AgendaElement;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.CommentMessageProvider;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@AllArgsConstructor
@Service
public class SurveyService {

    private final SurveyValidator surveyValidator;
    private final UserRepositoryService userRepositoryService;
    private final EventRepositoryService eventRepositoryService;
    private final CommentMessageProvider commentMessageProvider;
    private final SurveyRepository surveyRepository;


    public ResponseMessage addNewSurvey(Survey survey) throws RuntimeException {

        ResponseMessage responseMessage;
        responseMessage = surveyValidator.validate(survey);
        if (!responseMessage.isStatus()) {
            return responseMessage;
        }

        Event event = eventRepositoryService.findById(survey.getEventId()).orElseThrow(EventNotFoundException::new);

        User user = userRepositoryService.findById(survey.getUserId()).orElseThrow(UserNotFoundException::new);

        ResponseMessage agendaResponse = getAgendaElement(event.getAgenda(), survey.getSessionId());

        if (!agendaResponse.isStatus()) {
            agendaResponse.setReturnObject(survey);
            return agendaResponse;
        }

        AgendaElement agendaElement = (AgendaElement) agendaResponse.getReturnObject();

        // TODO: 10.08.2018 Validate questions somehow before save.
        survey.setSessionId(agendaElement.getId());
        survey.setEventId(event.getId());
        survey.setUserId(user.getId());
        survey.setAnswers(new ArrayList<>());
        survey.setTime(LocalDateTime.now());

        surveyRepository.save(survey);

        return new ResponseMessage(true, commentMessageProvider.getMessage("commentSavedSuccessfully"), survey);


    }

    private ResponseMessage getAgendaElement(List<AgendaElement> agendaElements, String agendaElementId) {
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
