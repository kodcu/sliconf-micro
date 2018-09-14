package javaday.istanbul.sliconf.micro.survey.controller.answer;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.survey.service.AnswerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@AllArgsConstructor
@Api(value = "survey", authorizations = {@Authorization(value = "Bearer")})
@Path("/service/events/:eventIdentifier/surveys/:surveyId/answer")
@Produces("application/json")
@Component
public class GetAnswers implements Route {

    private final AnswerService answerService;

    @GET
    @ApiOperation(value = "Gets all the answers that users have given to the survey.", nickname = "GetAnswersRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header",
                    example = "Authorization: Bearer <tokenValue>"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "userId", paramType = "path"),
            @ApiImplicitParam(required = true, dataType = "string", name = "surveyId", paramType = "path"),
            @ApiImplicitParam(required = true, dataType = "string", name = "eventIdentifier", paramType = "path")

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
        String eventIdentifier = request.params("eventIdentifier");
        String surveyId = request.params("surveyId");

        responseMessage = answerService.getSurveyAnswers(eventIdentifier, surveyId);

        return responseMessage;
    }
}
