package javaday.istanbul.sliconf.micro.controller.event.schedule;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.UserScheduleElement;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.event.agenda.AgendaElement;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.schedule.UserScheduleRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.util.json.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Api
@Path("/service/schedule/add")
@Produces("application/json")
@Component
public class AddToScheduleRoute implements Route {

    private UserScheduleRepositoryService userScheduleRepositoryService;
    private EventRepositoryService eventRepositoryService;
    private UserRepositoryService userRepositoryService;

    @Autowired
    public AddToScheduleRoute(UserScheduleRepositoryService userScheduleRepositoryService,
                              EventRepositoryService eventRepositoryService,
                              UserRepositoryService userRepositoryService) {
        this.userScheduleRepositoryService = userScheduleRepositoryService;
        this.eventRepositoryService = eventRepositoryService;
        this.userRepositoryService = userRepositoryService;
    }

    @POST
    @ApiOperation(value = "Adds an agenda element to users schedule", nickname = "AddToScheduleRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataTypeClass = UserScheduleElement.class, paramType = "body"), //
    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {
        String body = request.body();

        if (Objects.isNull(body) || body.isEmpty()) {
            return new ResponseMessage(false, "Body can not be empty!", new Object());
        }

        UserScheduleElement userScheduleElement = JsonUtil.fromJson(body, UserScheduleElement.class);

        return addToSchedule(userScheduleElement);
    }

    public ResponseMessage addToSchedule(UserScheduleElement userScheduleElement) {
        if (Objects.isNull(userScheduleElement)) {
            return new ResponseMessage(false, "User schedule Element can not be empty", new Object());
        }

        if (Objects.isNull(userScheduleElement.getEventId()) || userScheduleElement.getEventId().isEmpty()) {
            return new ResponseMessage(false, "Event Id can not be empty", new Object());
        }

        if (Objects.isNull(userScheduleElement.getSessionId()) || userScheduleElement.getSessionId().isEmpty()) {
            return new ResponseMessage(false, "Session Id can not be empty", new Object());
        }

        if (Objects.isNull(userScheduleElement.getUserId()) || userScheduleElement.getUserId().isEmpty()) {
            return new ResponseMessage(false, "User Id can not be empty", new Object());
        }

        if (Objects.nonNull(userScheduleElement.getId()) && !userScheduleElement.getId().isEmpty()) {
            return new ResponseMessage(false, "This element already added to schedule", userScheduleElement);
        }

        UserScheduleElement userScheduleElementFromDB = userScheduleRepositoryService.findByUserIdAndEventIdAndSessionId(userScheduleElement.getUserId(), userScheduleElement.getEventId(), userScheduleElement.getSessionId());

        if (Objects.nonNull(userScheduleElementFromDB)) {
            return new ResponseMessage(false, "This element already added to schedule", userScheduleElement);
        }

        User user = userRepositoryService.findById(userScheduleElement.getUserId());

        if (Objects.isNull(user)) {
            return new ResponseMessage(false, "User can not found with given id", userScheduleElement);
        }

        Event event = eventRepositoryService.findOne(userScheduleElement.getEventId());

        if (Objects.isNull(event)) {
            return new ResponseMessage(false, "Event can not found with given id", userScheduleElement);
        }

        if(!isAgendaElementExists(event.getAgenda(), userScheduleElement.getSessionId())) {
            return new ResponseMessage(false, "Session can not found with given id", userScheduleElement);
        }


        UserScheduleElement userScheduleElementSaved = userScheduleRepositoryService.save(userScheduleElement);

        if (Objects.isNull(userScheduleElementSaved)) {
            return new ResponseMessage(false, "Schedule can not saved to db", "");
        }

        List<UserScheduleElement> userScheduleElementList = userScheduleRepositoryService
                .findByUserIdAndEventId(userScheduleElement.getUserId(), userScheduleElement.getUserId());

        return new ResponseMessage(true, "Schedule element added", userScheduleElementList);
    }

    private boolean isAgendaElementExists(List<AgendaElement> agendaElements, String sessionId) {
        if (Objects.nonNull(agendaElements)) {
            List<AgendaElement> agendaElementsNew = agendaElements.stream().filter(agendaElement ->
                    Objects.nonNull(agendaElement) && Objects.nonNull(agendaElement.getId())
                            && agendaElement.getId().equals(sessionId)
            ).collect(Collectors.toList());

            return (Objects.nonNull(agendaElementsNew) && !agendaElementsNew.isEmpty() &&
                    Objects.nonNull(agendaElementsNew.get(0)));
        }

        return false;
    }
}
