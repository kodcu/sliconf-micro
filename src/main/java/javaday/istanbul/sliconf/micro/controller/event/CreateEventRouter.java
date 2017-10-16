package javaday.istanbul.sliconf.micro.controller.event;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.EventControllerMessageProvider;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.specs.EventSpecs;
import javaday.istanbul.sliconf.micro.util.JsonUtil;
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


@Api
@Path("/service/events/create/:userId")
@Produces("application/json")
@Component
public class CreateEventRouter implements Route {


    private EventControllerMessageProvider messageProvider;
    private EventRepositoryService repositoryService;

    @Autowired
    public CreateEventRouter(EventControllerMessageProvider messageProvider,
                             EventRepositoryService eventRepositoryService) {
        this.messageProvider = messageProvider;
        this.repositoryService = eventRepositoryService;
    }

    @POST
    @ApiOperation(value = "Creates an event and bind with given userId", nickname = "CreateEventRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "userId", paramType = "path"), //
            @ApiImplicitParam(required = true, dataTypeClass = Event.class, paramType = "body") //
    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {
        ResponseMessage responseMessage;

        String body = request.body();

        String userId = request.params("userId");

        if (Objects.isNull(userId)) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventUserIdCantBeEmpty"), new Object());
            return responseMessage;
        }

        if (Objects.isNull(body) || body.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventBodyCantBeEmpty"), new Object());
            return responseMessage;
        }

        Event event = JsonUtil.fromJson(body, Event.class);

        //isim uzunluğu minimumdan düşük mü diye kontrol et
        if (!EventSpecs.checkEventName(event, 4)) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventNameTooShort"), new Object());
            return responseMessage;
        }

        //event tarihinin geçip geçmediğin, kontrol et
        if (!EventSpecs.checkIfEventDateAfterOrInNow(event)) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventDataInvalid"), new Object());
            return responseMessage;
        }

        // event var mı diye kontrol et
        List<Event> dbEvents = repositoryService.findByName(event.getName());

        if (Objects.nonNull(dbEvents) && !dbEvents.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventAlreadyRegistered"), new Object());
            return responseMessage;
        }

        //Kanban numarası oluştur
        EventSpecs.generateKanbanNumber(event);

        event.setExecutiveUser(userId);

        // eger event yoksa kayit et
        ResponseMessage dbResponse = repositoryService.save(event);

        if (!dbResponse.isStatus()) {
            return dbResponse;
        }

        responseMessage = new ResponseMessage(true,
                messageProvider.getMessage("eventCreatedSuccessfully"), event);

        return responseMessage;
    }
}
