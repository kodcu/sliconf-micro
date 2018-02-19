package javaday.istanbul.sliconf.micro.controller.event.comment;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Comment;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.event.Room;
import javaday.istanbul.sliconf.micro.model.event.agenda.AgendaElement;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Api
@Path("/service/events/comment/add-new")
@Produces("application/json")
@Component
public class AddNewCommentRoute implements Route {


    private EventRepositoryService eventRepositoryService;
    private UserRepositoryService userRepositoryService;
    private CommentRepositoryService commentRepositoryService;

    private CommentMessageProvider commentMessageProvider;

    @Autowired
    public AddNewCommentRoute(EventRepositoryService eventRepositoryService,
                              CommentMessageProvider commentMessageProvider,
                              UserRepositoryService userRepositoryService,
                              CommentRepositoryService commentRepositoryService) {
        this.eventRepositoryService = eventRepositoryService;
        this.commentMessageProvider = commentMessageProvider;
        this.userRepositoryService = userRepositoryService;
        this.commentRepositoryService = commentRepositoryService;
    }

    @POST
    @ApiOperation(value = "Adds a new comment to a talk", nickname = "AddNewCommentRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataTypeClass = Comment.class, name = "comment", paramType = "body"), //
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

        Comment comment = JsonUtil.fromJson(body, Comment.class);

        return addNewComment(comment);
    }


    public ResponseMessage addNewComment(Comment comment) {

        if (!CommentSpecs.isAllFieldsAreSafeForProcess(comment)) {
            return new ResponseMessage(false,
                    commentMessageProvider.getMessage("commentValuesMustBeFilled"), comment);
        }

        ResponseMessage userCheckMessage = CommentSpecs.checkIfUserExists(comment.getUserId(), userRepositoryService, commentMessageProvider);

        if (!userCheckMessage.isStatus()) {
            return userCheckMessage;
        }

        User user = (User) userCheckMessage.getReturnObject();

        ResponseMessage eventCheckMessage = CommentSpecs.checkIfEventExists(comment.getEventId(), eventRepositoryService, commentMessageProvider);

        if (!eventCheckMessage.isStatus()) {
            return eventCheckMessage;
        }

        Event event = (Event) eventCheckMessage.getReturnObject();

        ResponseMessage agendaResponse = getAgendaElement(event.getAgenda(), comment.getSessionId());

        if (!agendaResponse.isStatus()) {
            agendaResponse.setReturnObject(comment);
            return agendaResponse;
        }

        AgendaElement agendaElement = (AgendaElement) agendaResponse.getReturnObject();

        ResponseMessage validMessage = checkIfCommentValid(agendaElement, comment);

        if (!validMessage.isStatus()) {
            validMessage.setReturnObject(comment);
            return validMessage;
        }

        this.setCommentValues(user, comment, agendaElement, event);

        Comment savedComment = commentRepositoryService.save(comment);

        if (Objects.isNull(savedComment)) {
            return new ResponseMessage(false, commentMessageProvider.getMessage("commentCanNotSaved"), comment);
        }

        return new ResponseMessage(true, commentMessageProvider.getMessage("commentSavedSuccessfully"), comment);
    }

    private void setCommentValues(User user, Comment comment, AgendaElement agendaElement, Event event) {
        if (Objects.nonNull(user)) {

            this.setCommentFullName(comment, user);

            comment.setUsername(user.getUsername());

            String roomName = "";
            String topic = "";

            if (Objects.nonNull(agendaElement)) {
                List<Room> roomList = event.getRooms().stream()
                        .filter(room -> Objects.nonNull(room) && Objects.nonNull(room.getId()) &&
                                room.getId().equals(agendaElement.getRoom()))
                        .collect(Collectors.toList());

                if (Objects.nonNull(roomList) && !roomList.isEmpty() &&
                        Objects.nonNull(roomList.get(0)) && Objects.nonNull(roomList.get(0).getLabel())) {
                    roomName = roomList.get(0).getLabel();
                }

                topic = agendaElement.getTopic();
            }


            comment.setRoomName(roomName);
            comment.setTopic(topic);
        }

        comment.setApproved("pending");
        comment.setLikes(new ArrayList<>());
        comment.setDislikes(new ArrayList<>());
    }

    private void setCommentFullName(Comment comment, User user) {
        if (Objects.nonNull(comment.getAnonymous()) && comment.getAnonymous()) {
            if (Objects.isNull(comment.getFullname()) || comment.getFullname().isEmpty()) {
                comment.setFullname("Anonymous");
            }
        } else {
            comment.setFullname(user.getFullname());
        }

    }

    private ResponseMessage checkIfCommentValid(AgendaElement agendaElement, Comment comment) {
        return isDateValid(agendaElement, comment.getTime());
    }

    private ResponseMessage isDateValid(AgendaElement agendaElement, LocalDateTime commentTime) {
        if (Objects.nonNull(agendaElement) && Objects.nonNull(commentTime)) {
            LocalDateTime endOfDay = agendaElement.getDate().toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);

            if ((agendaElement.getDate().isBefore(commentTime) || agendaElement.getDate().isEqual(commentTime)) && (commentTime.isBefore(endOfDay) || commentTime.isEqual(endOfDay))) {
                return new ResponseMessage(true, commentMessageProvider.getMessage("commentDateIsValid"), agendaElement);
            }
        }

        return new ResponseMessage(false, commentMessageProvider.getMessage("commentDateIsNotValid"), agendaElement);
    }

    private ResponseMessage getAgendaElement(List<AgendaElement> agendaElements, String agendaElementId) {
        if (Objects.nonNull(agendaElements)) {
            AgendaElement agendaElementReturn = agendaElements
                    .stream()
                    .filter(agendaElement -> Objects.nonNull(agendaElement) &&
                            Objects.nonNull(agendaElement.getId()) &&
                            agendaElement.getId().equals(agendaElementId)).findFirst().orElse(null);

            if (Objects.nonNull(agendaElementReturn)) {
                return new ResponseMessage(true, commentMessageProvider.getMessage("agendaElementFoundWithGivenId"), agendaElementReturn);
            }
        }

        return new ResponseMessage(false, commentMessageProvider.getMessage("agendaElementCanNotFoundWithGivenId"), agendaElementId);
    }

}
