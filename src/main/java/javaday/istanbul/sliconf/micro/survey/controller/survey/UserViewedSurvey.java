package javaday.istanbul.sliconf.micro.survey.controller.survey;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@Api(value = "survey", authorizations = {@Authorization(value = "Bearer")})
@Path("/service/events/:eventIdentifier/surveys/:surveyId/view")
@Produces("application/json")
@Component
public class UserViewedSurvey implements Route {

    private SurveyService surveyService;

    @POST
    @ApiOperation(value = "Save that user viewed the survey", nickname = "UserViewSurvey")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header",
                    example = "Authorization: Bearer <tokenValue>"), //
            @ApiImplicitParam(required = true, dataTypeClass = Map.Entry.class, name = "userId", paramType = "body"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "eventIdentifier", paramType = "request"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "surveyId", paramType = "request"), //

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

        Map<String, String> userIdFromBody = body.isEmpty() ? null : JsonUtil.fromJson(body, Map.class);

        String surveyId = request.params("surveyId");
        String eventIdentifier = request.params("eventIdentifier");

        String userId = Objects.isNull(userIdFromBody) ? null : userIdFromBody.values().stream().findFirst().orElse(null);

        responseMessage = surveyService.updateSurveyViewers(userId, surveyId, eventIdentifier);

        return responseMessage;
    }

}
