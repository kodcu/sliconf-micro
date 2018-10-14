package javaday.istanbul.sliconf.micro.user;

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
@Path("service/events/:eventIdentifier/users/:userId/answers")
@Api(value = "user", authorizations = {@Authorization(value = "Bearer" )})
@Produces("application/json")
@Component
public class GetSurveyAnswers implements Route {

    private UserService userService;

    @GET
    @ApiOperation(value = "Gets the user's answers to an event's surveys.", nickname = "GetSurveyAnswersOfUser")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header",
                    example = "Authorization: Bearer <tokenValue>"),
            @ApiImplicitParam(required = true, dataType = "string", name = "eventIdentifier", paramType = "path"),
            @ApiImplicitParam(required = true, dataType = "string", name = "userId", paramType = "path"),

    })

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
        String eventIdentifier = request.params("eventIdentifier");
        String userId = request.params("userId");

        responseMessage = userService.getSurveyAnswers(eventIdentifier, userId);
        return responseMessage;
    }
}
