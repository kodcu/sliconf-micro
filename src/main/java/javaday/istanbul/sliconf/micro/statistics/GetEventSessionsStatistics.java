package javaday.istanbul.sliconf.micro.statistics;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@AllArgsConstructor
@Api(value = "statistics", authorizations = {@Authorization(value = "Bearer")})
@Path("/service/events/:eventKey/statistics/sessions")
@Produces("application/json")
@Component
public class GetEventSessionsStatistics implements Route {

    private final StatisticsService statisticsService;

    @GET
    @ApiOperation(value = "Gets statistics for sessions of a event", nickname = "GetSessionStatistics")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header",
                    example = "Authorization: Bearer <tokenValue>"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "eventKey", paramType = "request"),


    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Örnekteki model ResponMessage sınıfı içindedeki returnObject olarak gönderilir.", response = EventSessionStatisticsDTO.class),
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })


    @Override
    public Object handle(@ApiParam(hidden = true) Request request,
                         @ApiParam(hidden = true) Response response) throws Exception {
        String eventKey = request.params("eventKey");
        ResponseMessage responseMessage;
        responseMessage = statisticsService.getEventSessionsStatistics(eventKey);

        return responseMessage;
    }
}
