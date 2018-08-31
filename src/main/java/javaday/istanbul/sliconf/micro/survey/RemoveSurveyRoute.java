package javaday.istanbul.sliconf.micro.survey;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@AllArgsConstructor
@Api
@Path("/service/events/survey/delete/:userId/:surveyId")
@Produces("application/json")
@Component
public class RemoveSurveyRoute implements Route {

    private final SurveyService surveyService;

    @DELETE
    @ApiOperation(value = "Remove a survey from session.", nickname = "RemoveSurveyRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token",    paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "userId",   paramType = "path"),
            @ApiImplicitParam(required = true, dataType = "string", name = "surveyId", paramType = "path")
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
        String userId = request.params("userId");
        String surveyId = request.params("surveyId");

        responseMessage = surveyService.deleteSurvey(userId, surveyId);
        return responseMessage;
    }
}
