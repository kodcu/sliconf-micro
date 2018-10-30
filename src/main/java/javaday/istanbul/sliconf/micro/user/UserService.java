package javaday.istanbul.sliconf.micro.user;

import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.survey.service.AnswerService;
import javaday.istanbul.sliconf.micro.survey.service.GeneralService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserService {
    private final GeneralService generalService;
    private final AnswerService answerService;

    ResponseMessage getSurveyAnswers(String eventIdentifier, String userId) {

        Event event = (Event) generalService.findEventByIdOrEventKey(eventIdentifier).getReturnObject();
        generalService.findUserById(userId);

        return answerService.getEventSurveysAnswersOfUser(event.getId(), userId);

    }
}
