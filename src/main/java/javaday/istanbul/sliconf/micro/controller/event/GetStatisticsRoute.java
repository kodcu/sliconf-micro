package javaday.istanbul.sliconf.micro.controller.event;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.event.Comment;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.event.agenda.AgendaElement;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.EventControllerMessageProvider;
import javaday.istanbul.sliconf.micro.service.comment.CommentRepositoryService;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.*;
import java.util.stream.Collectors;

@Api
@Path("/service/events/get/statistics/:key")
@Produces("application/json")
@Component
public class GetStatisticsRoute implements Route {

    private EventControllerMessageProvider messageProvider;
    private EventRepositoryService repositoryService;
    private CommentRepositoryService commentRepositoryService;

    @Autowired
    public GetStatisticsRoute(EventControllerMessageProvider messageProvider,
                              EventRepositoryService eventRepositoryService,
                              CommentRepositoryService commentRepositoryService) {
        this.messageProvider = messageProvider;
        this.repositoryService = eventRepositoryService;
        this.commentRepositoryService = commentRepositoryService;
    }

    @GET
    @ApiOperation(value = "Returns event statistics with given key", nickname = "GetStatisticsRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "key", paramType = "path"),
    })
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {
        ResponseMessage responseMessage;

        String key = request.params("key");

        // event var mı diye kontrol et
        Event event = repositoryService.findEventByKeyEquals(key);

        if (Objects.isNull(event)) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventCanNotFound"), new Object());
            return responseMessage;
        }

        Map<String, Object> statisticsMap = new HashMap<>();

        long approvedComments = getCommentCount(event, "approved", "");
        long deniedComments = getCommentCount(event, "denied", "");

        Comment mostLikedComment = getMostLikedComment(event, "approved");

        mostLikedComment.setLikes(new ArrayList<>());
        mostLikedComment.setDislikes(new ArrayList<>());

        String mostCommentedSessionId = commentRepositoryService.findMostCommentedSessionId("approved", event.getId());

        List<AgendaElement> agendaElements = event.getAgenda().stream().filter(agendaElement -> Objects.nonNull(agendaElement) &&
                Objects.nonNull(agendaElement.getId()) && agendaElement.getId().equals(mostCommentedSessionId)).collect(Collectors.toList());

        if (Objects.nonNull(agendaElements) && !agendaElements.isEmpty() && Objects.nonNull(agendaElements.get(0))) {
            statisticsMap.put("mostCommentedSession", agendaElements.get(0));
        }

        statisticsMap.put("approvedComments", approvedComments);
        statisticsMap.put("deniedComments", deniedComments);
        statisticsMap.put("mostLikedComment", mostLikedComment);
        statisticsMap.put("totalUsers", event.getTotalUsers());


        responseMessage = new ResponseMessage(true,
                messageProvider.getMessage("eventStatisticsQueried"), statisticsMap);

        return responseMessage;
    }

    private long getCommentCount(Event event, String status, String type) {
        if (Objects.nonNull(event)) {
            List<Comment> commentList = null;
            if (Objects.nonNull(event.getId()) && Objects.nonNull(status) && !status.isEmpty()) {
                commentList = commentRepositoryService.findAllByStatusAndEventId(status, event.getId(), 0, type);
            }

            return Objects.nonNull(commentList) ? commentList.size() : 0;
        }
        return 0;
    }

    private Comment getMostLikedComment(Event event, String status) {
        if (Objects.nonNull(event)) {
            Comment comment = null;
            if (Objects.nonNull(event.getId()) && Objects.nonNull(status) && !status.isEmpty()) {
                comment = commentRepositoryService.findMostLikedComment(status, event.getId());
            }

            return Objects.nonNull(comment) ? comment : new Comment();
        }
        return new Comment();
    }
}
