package javaday.istanbul.sliconf.micro.controller.event.comment;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.event.Comment;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.CommentMessageProvider;
import javaday.istanbul.sliconf.micro.service.comment.CommentRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.Objects;


@Api
@Path("/service/events/comment/list/:eventId/:sessionId/:userId/:status")
@Produces("application/json")
@Component
public class ListCommentsRoute implements Route {
    private CommentRepositoryService commentRepositoryService;
    private CommentMessageProvider commentMessageProvider;

    @Autowired
    public ListCommentsRoute(CommentMessageProvider commentMessageProvider,
                             CommentRepositoryService commentRepositoryService) {
        this.commentMessageProvider = commentMessageProvider;
        this.commentRepositoryService = commentRepositoryService;
    }

    @GET
    @ApiOperation(value = "Lists comments", nickname = "ListCommentsRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "eventId", paramType = "path"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "sessionId", paramType = "path"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "userId", paramType = "path"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "status", paramType = "path", example = "denied, pending, approved"), //
    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {
        String eventId = request.params("eventId");
        String sessionId = request.params("sessionId");
        String userId = request.params("userId");

        String status = request.params("status");

        return listComments(eventId, sessionId, userId, status);
    }


    public ResponseMessage listComments(String eventId, String sessionId, String userId, String status) {

        List<Comment> comments = null;

        if (Objects.nonNull(eventId) && !eventId.isEmpty()) {
            if (Objects.nonNull(sessionId) && !sessionId.isEmpty()) {
                if (Objects.nonNull(userId) && !userId.isEmpty()) {

                    if (Objects.nonNull(status) && !status.isEmpty()) {
                        comments = commentRepositoryService.findAllByEventIdAndSessionIdAndUserIdAndStatus(eventId, sessionId, userId, status);
                    } else {
                        comments = commentRepositoryService.findAllByEventIdAndSessionIdAndUserId(eventId, sessionId, userId);
                    }
                } else {
                    comments = commentRepositoryService.findAllByEventIdAndSessionId(eventId, sessionId);
                }
            } else {
                comments = commentRepositoryService.findAllByEventId(eventId);
            }
        }

        if (Objects.nonNull(comments)) {
            return new ResponseMessage(true, commentMessageProvider.getMessage("commentsAreQueried"), comments);
        }

        return new ResponseMessage(false, commentMessageProvider.getMessage("commentsCanNotFound"), new Object());
    }
}
