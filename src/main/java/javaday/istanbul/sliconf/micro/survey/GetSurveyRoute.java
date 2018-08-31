package javaday.istanbul.sliconf.micro.survey;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@AllArgsConstructor
@Api
@Path("/service/events/get/:eventId/:sessionId")
@Produces("application/json")
@Component
public class GetSurveyRoute implements Route {

    private final SurveyService surveyService;

    @GET
    @ApiOperation(value = "Get surveys of specific session.", nickname = "GetSurveyRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token",    paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "eventId",   paramType = "path"),
            @ApiImplicitParam(required = true, dataType = "string", name = "sessionId", paramType = "path"),
    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    @Override
    public ResponseMessage handle(Request request, Response response) throws Exception {
        ResponseMessage responseMessage;
        String eventId = request.params("eventId");
        String sessionId = request.params("sessionId");

        responseMessage = surveyService.getSurveysByEventIdAndSessionId(eventId, sessionId);
        return responseMessage;

    }
}
