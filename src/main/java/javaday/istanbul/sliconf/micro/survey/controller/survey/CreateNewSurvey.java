package javaday.istanbul.sliconf.micro.survey.controller.survey;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.survey.model.Survey;
import javaday.istanbul.sliconf.micro.survey.service.SurveyService;
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
@Api(value = "survey", authorizations = {@Authorization(value = "Bearer" )})
@Path("/service/events/:eventIdentifier/surveys/")
@Produces("application/json")
@Component
public class CreateNewSurvey implements Route {

    private final SurveyService surveyService;

    @POST
    @ApiOperation(value = "Adds a new survey to a event", nickname = "AddNewSurveyRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header",
                    example = "Authorization: Bearer <tokenValue>"), //
            @ApiImplicitParam(required = true, dataTypeClass = Survey.class, name = "survey", paramType = "body"), //
            @ApiImplicitParam(required = true, dataType= "string", name = "eventIdentifier", paramType = "request"), //

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
        String body = request.body();
        String eventIdentifier = request.params("eventIdentifier");

        if (Objects.isNull(body) || body.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "Body can not be empty!", new Object());
            return responseMessage;
        }

        Survey survey = JsonUtil.fromJson(body, Survey.class);

        responseMessage = surveyService.addNewSurvey(survey, eventIdentifier);
        return responseMessage;
    }

}
