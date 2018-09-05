package javaday.istanbul.sliconf.micro.survey.routes.answer;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.survey.model.Answer;
import javaday.istanbul.sliconf.micro.survey.service.AnswerService;
import javaday.istanbul.sliconf.micro.survey.service.SurveyService;
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
@Path("/service/events/:eventKey/survey/:surveyId/answer/get-answers/:userId")
@Produces("application/json")
@Component
public class GetAnswers implements Route {

    private final AnswerService answerService;

    @GET
    @ApiOperation(value = "Get answers of a survey.", nickname = "GetAnswersRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "userId", paramType = "path"),
            @ApiImplicitParam(required = true, dataType = "string", name = "surveyId", paramType = "path"),
            @ApiImplicitParam(required = true, dataType = "string", name = "eventKey", paramType = "path"),
            @ApiImplicitParam(required = true, dataTypeClass = Answer.class, name = "answer", paramType = "body"), //

    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })

    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true)  Response response)
            throws Exception {

        ResponseMessage responseMessage;
        String eventKey = request.params("eventKey");
        String surveyId = request.params("surveyId");

        responseMessage = answerService.getSurveyAnswers(eventKey, surveyId);

        return responseMessage;
    }
}
