package javaday.istanbul.sliconf.micro.survey.controller.answer;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.event.service.EventRepositoryService;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.survey.model.Answer;
import javaday.istanbul.sliconf.micro.survey.service.AnswerService;
import javaday.istanbul.sliconf.micro.survey.service.GeneralService;
import javaday.istanbul.sliconf.micro.util.json.JsonUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Objects;

@AllArgsConstructor
@Api(value = "survey", authorizations = {@Authorization(value = "Bearer")})
@Path("/service/events/:eventIdentifier/surveys/:surveyId/answers")
@Produces("application/json")
@Component
public class SubmitAnswers implements Route {

    private final AnswerService answerService;

    private final GeneralService generalService;

    private final EventRepositoryService eventRepositoryService;

    @POST
    @ApiOperation(value = "Submits the user's answers to a specific survey.", nickname = "SubmitSurveyAnswersRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header",
                    example = "Authorization: Bearer <tokenValue>"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "surveyId", paramType = "path"),
            @ApiImplicitParam(required = true, dataTypeClass = Answer.class, name = "answer", paramType = "body"), //

    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })

    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request,
                                  @ApiParam(hidden = true) Response response) throws Exception {

        ResponseMessage responseMessage;
        String surveyId = request.params("surveyId");
        String eventIdentifier = request.params("eventIdentifier");
        String body = request.body();
        if (Objects.isNull(body) || body.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "Body can not be empty!", new Object());
            return responseMessage;
        }
        Answer answer = JsonUtil.fromJsonOrElseThrow(body, Answer.class);
        responseMessage = answerService.answerSurvey(answer, surveyId, eventIdentifier);
        return responseMessage;
    }
}
