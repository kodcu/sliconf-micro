package javaday.istanbul.sliconf.micro.controller.event;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.EventControllerMessageProvider;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Objects;


@Api
@Path("/service/events/delete/:eventKey/:userId")
@Produces("application/json")
@Component
public class DeleteEventRoute implements Route {


    private EventControllerMessageProvider messageProvider;
    private EventRepositoryService eventRepositoryService;

    private UserRepositoryService userRepositoryService;

    @Autowired
    public DeleteEventRoute(EventControllerMessageProvider messageProvider,
                            EventRepositoryService eventRepositoryService,
                            UserRepositoryService userRepositoryService) {
        this.messageProvider = messageProvider;
        this.eventRepositoryService = eventRepositoryService;
        this.userRepositoryService = userRepositoryService;
    }

    @DELETE
    @ApiOperation(value = "Deletes an event", nickname = "DeleteEventRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "userId", paramType = "path"),
            @ApiImplicitParam(required = true, dataType = "string", name = "eventKey", paramType = "path")
    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {
        String userId = request.params("userId");
        String eventKey = request.params("eventKey");

        return deleteEvent(eventKey, userId);
    }

    public ResponseMessage deleteEvent(String eventKey, String userId) {
        ResponseMessage responseMessage;

        if (Objects.isNull(userId) || userId.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventUserIdCantBeEmpty"), new Object());
            return responseMessage;
        }

        if (Objects.isNull(eventKey) || eventKey.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventKeyCantBeEmpty"), new Object());
            return responseMessage;
        }

        if (eventKey.toLowerCase().contains("deleted")) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventAlreadyDeletedOrDoNotExists"), new Object());
            return responseMessage;
        }

        User user = userRepositoryService.findById(userId).get();

        if (Objects.isNull(user)) {
            responseMessage = new ResponseMessage(false,
                    "User can not found with given id", new Object());
            return responseMessage;
        }

        Event event = eventRepositoryService.findByKeyAndExecutiveUser(eventKey, userId);

        if (Objects.isNull(event)) {
            responseMessage = new ResponseMessage(false,
                    "Event can not found with given key and user!", new Object());
            return responseMessage;
        }

        event.setDeleted(true);

        responseMessage = eventRepositoryService.save(event);

        if (!responseMessage.isStatus()) {
            responseMessage.setMessage("Event can not deleted");
            return responseMessage;
        }

        responseMessage.setMessage("Event deleted successfully");

        return responseMessage;
    }
}
