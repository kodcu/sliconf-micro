package javaday.istanbul.sliconf.micro.controller.event.comment;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.event.Comment;
import javaday.istanbul.sliconf.micro.model.response.ListCommentResponseMessage;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.CommentMessageProvider;
import javaday.istanbul.sliconf.micro.service.comment.CommentRepositoryService;
import javaday.istanbul.sliconf.micro.specs.CommentSpecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Path("/service/events/comment/list/:status/:eventId/:sessionId/:userId")
@Produces("application/json")
@Component
public class ListCommentsRoute implements Route {
    private CommentRepositoryService commentRepositoryService;
    private CommentMessageProvider commentMessageProvider;

    private Logger logger = LoggerFactory.getLogger(ListCommentsRoute.class);

    @Autowired
    public ListCommentsRoute(CommentMessageProvider commentMessageProvider,
                             CommentRepositoryService commentRepositoryService) {
        this.commentMessageProvider = commentMessageProvider;
        this.commentRepositoryService = commentRepositoryService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GET
    @ApiOperation(value = "Lists comments", nickname = "ListCommentsRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "eventId", paramType = "path"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "sessionId", paramType = "path"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "userId", paramType = "path"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "status", paramType = "path", example = "denied, pending, approved"), //
            @ApiImplicitParam(dataType = "int", name = "count", paramType = "query"), //
            @ApiImplicitParam(dataType = "string", name = "type", paramType = "query", example = "top-rated, recent, oldest"), //
            @ApiImplicitParam(dataType = "int", name = "page", paramType = "query", example = "1,2,5,10,100"), //
            @ApiImplicitParam(dataType = "string", name = "clientType", paramType = "query", example = "web"), //
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

        String count = request.queryParams("count");
        String type = request.queryParams("type");
        String page = request.queryParams("page");
        String clientType = request.queryParams("clientType");

        ResponseMessage responseMessage = listComments(eventId, sessionId, userId, status, count, type, page);

        if ("web".equals(clientType)) {
            ListCommentResponseMessage listCommentResponseMessage = new ListCommentResponseMessage();

            listCommentResponseMessage.setStatus(responseMessage.isStatus());
            listCommentResponseMessage.setMessage(responseMessage.getMessage());
            listCommentResponseMessage.setReturnObject(responseMessage.getReturnObject());
            listCommentResponseMessage.setCommentType(status);

            return listCommentResponseMessage;
        }

        return responseMessage;
    }


    public ResponseMessage listComments(String eventId, String sessionId, String userId, String status, String countString, String type, String pageString) {

        List<Comment> comments;
        int count = 0;
        int page = 0;

        if (Objects.nonNull(type) && !CommentSpecs.isCommentTypeValid(type)) {
            return new ResponseMessage(false, "Comment type must be a valid type", new Object());

        }

        if (Objects.nonNull(countString)) {
            try {
                count = Integer.parseInt(countString);
            } catch (NumberFormatException e) {
                logger.error(e.getMessage(), e);
            }
        }

        if (Objects.nonNull(pageString)) {
            try {
                page = Integer.parseInt(pageString);
            } catch (NumberFormatException e) {
                logger.error(e.getMessage(), e);
            }
        }

        if (Objects.nonNull(status) && !status.isEmpty()) {
            comments = getComments(eventId, sessionId, userId, status, type, count, page);
        } else {
            return new ResponseMessage(false, commentMessageProvider.getMessage("youNeedTheSpecifyStatusOfComment"), new Object());
        }

        if (Objects.nonNull(comments)) {
            return new ResponseMessage(true, commentMessageProvider.getMessage("commentsAreQueried"), comments);
        }

        return new ResponseMessage(false, commentMessageProvider.getMessage("commentsCanNotFound"), new Object());
    }

    private List<Comment> getComments(String eventId, String sessionId, String userId, String status, String type, int count, int page) {
        List<Comment> comments = null;
        if (Objects.nonNull(eventId) && !eventId.isEmpty()) {
            if (Objects.nonNull(sessionId) && !sessionId.isEmpty()) {
                if (Objects.nonNull(userId) && !userId.isEmpty()) {
                    comments = commentRepositoryService.findAllByStatusAndEventIdAndSessionIdAndUserId(status, eventId, sessionId, userId, count, type, page);
                } else {
                    comments = commentRepositoryService.findAllByStatusAndEventIdAndSessionId(status, eventId, sessionId, count, type, page);
                }
            } else {
                comments = commentRepositoryService.findAllByStatusAndEventId(status, eventId, count, type, page);
            }
        }
        return comments;
    }
}
