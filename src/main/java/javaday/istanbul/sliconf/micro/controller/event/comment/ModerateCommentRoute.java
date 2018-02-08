package javaday.istanbul.sliconf.micro.controller.event.comment;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.ModerateCommentModel;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Comment;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.CommentMessageProvider;
import javaday.istanbul.sliconf.micro.service.comment.CommentRepositoryService;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.specs.CommentSpecs;
import javaday.istanbul.sliconf.micro.util.json.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.Objects;


@Api
@Path("/service/events/comment/moderate")
@Produces("application/json")
@Component
public class ModerateCommentRoute implements Route {


    private EventRepositoryService eventRepositoryService;
    private UserRepositoryService userRepositoryService;
    private CommentRepositoryService commentRepositoryService;

    private CommentMessageProvider commentMessageProvider;

    private static final String MODERATE_MODEL_MUST_BE_FILLED_CORRECTLY = "moderateModelMustBeFilledCorrectly";
    private static final String COMMENTS_UPDATED_SUCCESSFULLY = "commentsUpdatedSuccessfully";

    @Autowired
    public ModerateCommentRoute(EventRepositoryService eventRepositoryService,
                                CommentMessageProvider commentMessageProvider,
                                UserRepositoryService userRepositoryService,
                                CommentRepositoryService commentRepositoryService) {
        this.eventRepositoryService = eventRepositoryService;
        this.commentMessageProvider = commentMessageProvider;
        this.userRepositoryService = userRepositoryService;
        this.commentRepositoryService = commentRepositoryService;
    }

    @POST
    @ApiOperation(value = "Moderates comments", nickname = "ModerateCommentRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataTypeClass = ModerateCommentModel.class, name = "moderate", paramType = "body"), //
    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {
        ResponseMessage responseMessage;

        String body = request.body();

        if (Objects.isNull(body) || body.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "Body can not be empty!", new Object());
            return responseMessage;
        }

        ModerateCommentModel moderateCommentModel = JsonUtil.fromJson(body, ModerateCommentModel.class);

        return moderateComment(moderateCommentModel);
    }


    public ResponseMessage moderateComment(ModerateCommentModel model) {

        if (!CommentSpecs.isModerateCommentModelSafeForProcess(model)) {
            return new ResponseMessage(false,
                    commentMessageProvider.getMessage(MODERATE_MODEL_MUST_BE_FILLED_CORRECTLY), model);
        }

        ResponseMessage userCheckMessage = CommentSpecs.checkIfUserExists(model.getUserId(), userRepositoryService, commentMessageProvider);

        if (!userCheckMessage.isStatus()) {
            return userCheckMessage;
        }

        User user = (User) userCheckMessage.getReturnObject();

        ResponseMessage eventCheckMessage = CommentSpecs.checkIfEventExists(model.getEventId(), eventRepositoryService, commentMessageProvider);

        if (!eventCheckMessage.isStatus()) {
            return eventCheckMessage;
        }

        Event event = (Event) eventCheckMessage.getReturnObject();

        if (Objects.isNull(event.getExecutiveUser()) ||
                !event.getExecutiveUser().equals(user.getId())) {
            return new ResponseMessage(false, "You are not a executive user!", model);
        }

        ResponseMessage approvedResponseMessage = processApprovedComments(model);

        if (!approvedResponseMessage.isStatus()) {
            return approvedResponseMessage;
        }

        ResponseMessage deniedResponseMessage = processDeniedComments(model);

        if (!deniedResponseMessage.isStatus()) {
            return deniedResponseMessage;
        }

        return new ResponseMessage(true, commentMessageProvider.getMessage(COMMENTS_UPDATED_SUCCESSFULLY), model);
    }

    private ResponseMessage processApprovedComments(ModerateCommentModel model) {
        if (Objects.nonNull(model) && Objects.nonNull(model.getApproved())) {
            List<String> approvedCommentIds = model.getApproved();

            approvedCommentIds.forEach(commentId -> {
                if (Objects.nonNull(commentId)) {
                    Comment comment = commentRepositoryService.findById(commentId);

                    if (Objects.nonNull(comment)) {
                        comment.setApproved("approved");
                        commentRepositoryService.save(comment);
                    }
                }
            });
            return new ResponseMessage(true, commentMessageProvider.getMessage(COMMENTS_UPDATED_SUCCESSFULLY), model);
        }
        return new ResponseMessage(false, commentMessageProvider.getMessage(MODERATE_MODEL_MUST_BE_FILLED_CORRECTLY), model);
    }

    private ResponseMessage processDeniedComments(ModerateCommentModel model) {
        if (Objects.nonNull(model) && Objects.nonNull(model.getDenied())) {
            List<String> deniedCommentIds = model.getDenied();

            deniedCommentIds.forEach(commentId -> {
                if (Objects.nonNull(commentId)) {
                    Comment comment = commentRepositoryService.findById(commentId);

                    if (Objects.nonNull(comment)) {
                        comment.setApproved("denied");
                        commentRepositoryService.save(comment);
                    }
                }
            });
            return new ResponseMessage(true, commentMessageProvider.getMessage(COMMENTS_UPDATED_SUCCESSFULLY), model);
        }
        return new ResponseMessage(false, commentMessageProvider.getMessage(MODERATE_MODEL_MUST_BE_FILLED_CORRECTLY), model);
    }
}
