package javaday.istanbul.sliconf.micro.survey;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
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
@Api
@Path("/service/events/answer/:userId/:surveyId")
@Produces("application/json")
@Component
public class AnswerSurveyRoute implements Route {

    private final SurveyService surveyService;

    @POST
    @ApiOperation(value = "Answer a survey.", nickname = "AnswerSurveyRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token",    paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "userId",   paramType = "path"),
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
    public ResponseMessage handle(Request request, Response response) throws Exception {
        ResponseMessage responseMessage;
        String userId = request.params("userId");
        String surveyId = request.params("surveyId");

        String body = request.body();

        if (Objects.isNull(body) || body.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "Body can not be empty!", new Object());
            return responseMessage;
        }

        Answer answer = JsonUtil.fromJson(body, Answer.class);

        // TODO: 01.09.2018 Validate answers.
        responseMessage = surveyService.answerSurvey(answer, userId, surveyId);
        return responseMessage;
    }
}
