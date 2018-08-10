package javaday.istanbul.sliconf.micro.survey;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.exceptions.EventNotFoundException;
import javaday.istanbul.sliconf.micro.exceptions.UserNotFoundException;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.CommentMessageProvider;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.util.json.JsonUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Objects;

@AllArgsConstructor
@Api
@Path("/service/events/survey/add-new")
@Produces("application/json")
@Component
public class AddNewSurveyRoute implements Route {

    private final SurveyService surveyService;


    @POST
    @ApiOperation(value = "Adds a new survey to a event", nickname = "AddNewSurveyRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataTypeClass = Survey.class, name = "survey", paramType = "body"), //
    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })


    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response)
            throws Exception {

        ResponseMessage responseMessage;
        String body = request.body();

        if (Objects.isNull(body) || body.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "Body can not be empty!", new Object());
            return responseMessage;
        }

        Survey survey = JsonUtil.fromJson(body, Survey.class);

        return surveyService.addNewSurvey(survey);
    }




    @ExceptionHandler(EventNotFoundException.class)
    ResponseMessage handleEventNotFoundException(Event event, CommentMessageProvider commentMessageProvider) {
        return new ResponseMessage(false, commentMessageProvider.getMessage("eventCanNotFoundWithGivenId"), event.getId());

    }

    @ExceptionHandler(UserNotFoundException.class)
    ResponseMessage handleUserNotFoundException(User user, CommentMessageProvider commentMessageProvider) {
        return new ResponseMessage(false, commentMessageProvider.getMessage("userCanNotFoundWithGivenId"), user.getId());

    }


}
