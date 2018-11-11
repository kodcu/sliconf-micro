package javaday.istanbul.sliconf.micro.comment.controller;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.comment.CommentRepositoryService;
import javaday.istanbul.sliconf.micro.comment.VoteMessageProvider;
import javaday.istanbul.sliconf.micro.comment.model.Comment;
import javaday.istanbul.sliconf.micro.comment.model.VoteUser;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import javaday.istanbul.sliconf.micro.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Api
@Path("/service/events/comment/vote/:commentId/:userId/:voteValue")
@Produces("application/json")
@Component
public class VoteCommentRoute implements Route {

    private UserRepositoryService userRepositoryService;
    private CommentRepositoryService commentRepositoryService;

    private VoteMessageProvider voteMessageProvider;

    private Logger logger = LoggerFactory.getLogger(VoteCommentRoute.class);

    @Autowired
    public VoteCommentRoute(VoteMessageProvider voteMessageProvider,
                            UserRepositoryService userRepositoryService,
                            CommentRepositoryService commentRepositoryService) {
        this.voteMessageProvider = voteMessageProvider;
        this.userRepositoryService = userRepositoryService;
        this.commentRepositoryService = commentRepositoryService;
    }

    @POST
    @ApiOperation(value = "Votes a comment", nickname = "VoteCommentRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "commentId", paramType = "path"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "userId", paramType = "path"), //
            @ApiImplicitParam(required = true, dataType = "int", name = "voteValue", paramType = "path", allowableValues = "-1,0,1"), //
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

        String commentId = request.params("commentId");
        String userId = request.params("userId");
        String stringVote = request.params("voteValue");

        if (Objects.isNull(commentId) || commentId.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "CommentId can not be empty!", new Object());
            return responseMessage;
        }

        if (Objects.isNull(userId) || userId.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "userId can not be empty!", new Object());
            return responseMessage;
        }

        if (Objects.isNull(stringVote) || stringVote.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "vote can not be empty!", new Object());
            return responseMessage;
        }

        boolean isNumberSafe = true;
        int vote = 0;

        try {
            vote = Integer.parseInt(stringVote);
        } catch (NumberFormatException e) {
            isNumberSafe = false;
            logger.error(e.getMessage(), e);
        }

        if (isNumberSafe) {
            return voteComment(commentId, userId, vote);
        } else {
            return new ResponseMessage(false, "Vote must be integer value", stringVote);
        }
    }


    public ResponseMessage voteComment(String commentId, String userId, int vote) {

        ResponseMessage userResponseMessage = checkIfUserExists(userId);

        if (!userResponseMessage.isStatus()) {
            return userResponseMessage;
        }

        ResponseMessage commentResponseMessage = checkIfCommentExists(commentId);

        if (!commentResponseMessage.isStatus()) {
            return commentResponseMessage;
        }

        User user = (User) userResponseMessage.getReturnObject();

        Comment comment = (Comment) commentResponseMessage.getReturnObject();

        if (!isCommentAndUserSafeForNull(comment, user)) {
            return new ResponseMessage(false, "Comment and User can not be null", "");
        }

        if (!isCommentApproved(comment)) {
            return new ResponseMessage(false, "Can not vote to not approved comment", comment);
        }

        if (isCommentOwnedByUser(comment, user)) {
            return new ResponseMessage(false, "User can not vote her/his comment", comment);
        }

        ResponseMessage voteResponseMessage;

        // Dislike
        if (vote == Constants.COMMENT_VOTES.DISLIKE.getValue()) {
            voteResponseMessage = voteDislike(comment, user);
        } else if (vote == Constants.COMMENT_VOTES.LIKE.getValue()) {
            voteResponseMessage = voteLike(comment, user);
        } else {
            //notr
            voteResponseMessage = voteNotr(comment, user);
        }

        if (!voteResponseMessage.isStatus()) {
            return voteResponseMessage;
        }

        comment.setRate(comment.getLike() - comment.getDislike());

        Comment savedComment = commentRepositoryService.save(comment);

        if (Objects.isNull(savedComment)) {
            return new ResponseMessage(false, "Comment can not saved", savedComment);
        }

        return new ResponseMessage(true, "Comment voted", comment);
    }

    private ResponseMessage checkIfUserExists(String userId) {
        User user = userRepositoryService.findById(userId).orElse(null);

        if (Objects.nonNull(user)) {
            return new ResponseMessage(true, voteMessageProvider.getMessage("userFoundWithGivenId"), user);
        } else {
            return new ResponseMessage(false, voteMessageProvider.getMessage("userCanNotFoundWithGivenId"), userId);
        }
    }

    private ResponseMessage checkIfCommentExists(String commentId) {
        Comment comment = commentRepositoryService.findById(commentId);

        if (Objects.nonNull(comment)) {
            return new ResponseMessage(true, voteMessageProvider.getMessage("commentFoundWithGivenId"), comment);
        } else {
            return new ResponseMessage(false, voteMessageProvider.getMessage("commentCanNotFoundWithGivenId"), commentId);
        }
    }

    private ResponseMessage voteDislike(Comment comment, User user) {
        List<VoteUser> dislikeVoteUsers = comment.getDislikes();

        if (isUserInList(dislikeVoteUsers, user)) {
            return new ResponseMessage(false, voteMessageProvider.getMessage("userAlreadyDislikedThisComment"), comment);
        }

        List<VoteUser> likeVoteUsers = comment.getLikes();

        List<VoteUser> newLikeVoteUsers;

        boolean isUserInLikeList = isUserInList(likeVoteUsers, user);

        if (isUserInLikeList) {
            newLikeVoteUsers = removeFromList(likeVoteUsers, user);

            if (comment.getLike() > 0) {
                comment.setLike(comment.getLike() - 1);
            }
        } else {
            newLikeVoteUsers = likeVoteUsers;
        }

        List<VoteUser> newDislikeVoteUser = addToList(dislikeVoteUsers, user);

        comment.setDislike(comment.getDislike() + 1);
        comment.setDislikes(newDislikeVoteUser);

        comment.setLikes(newLikeVoteUsers);

        return new ResponseMessage(true, "Comment disliked", comment);
    }

    private ResponseMessage voteLike(Comment comment, User user) {
        List<VoteUser> likeVoteUsers = comment.getLikes();

        if (isUserInList(likeVoteUsers, user)) {
            return new ResponseMessage(false, voteMessageProvider.getMessage("userAlreadyLikedThisComment"), comment);
        }

        List<VoteUser> dislikeVoteUsers = comment.getDislikes();

        List<VoteUser> newDislikeVoteUsers;

        boolean isUserInDislikeList = isUserInList(dislikeVoteUsers, user);

        if (isUserInDislikeList) {
            newDislikeVoteUsers = removeFromList(dislikeVoteUsers, user);

            if (comment.getDislike() > 0) {
                comment.setDislike(comment.getDislike() - 1);
            }
        } else {
            newDislikeVoteUsers = dislikeVoteUsers;
        }

        List<VoteUser> newLikeVoteUser = addToList(likeVoteUsers, user);

        comment.setLike(comment.getLike() + 1);
        comment.setLikes(newLikeVoteUser);

        comment.setDislikes(newDislikeVoteUsers);

        return new ResponseMessage(true, "Comment liked", comment);
    }

    private ResponseMessage voteNotr(Comment comment, User user) {
        List<VoteUser> likeVoteUsers = comment.getLikes();

        List<VoteUser> dislikeVoteUsers = comment.getDislikes();

        List<VoteUser> newDislikeVoteUsers;
        List<VoteUser> newLikeVoteUsers;

        if (isUserInList(likeVoteUsers, user)) {

            newLikeVoteUsers = removeFromList(likeVoteUsers, user);

            if (comment.getLike() > 0) {
                comment.setLike(comment.getLike() - 1);
            }

            newDislikeVoteUsers = dislikeVoteUsers;

        } else if (isUserInList(dislikeVoteUsers, user)) {
            newDislikeVoteUsers = removeFromList(dislikeVoteUsers, user);

            if (comment.getDislike() > 0) {
                comment.setDislike(comment.getDislike() - 1);
            }

            newLikeVoteUsers = likeVoteUsers;
        } else {
            return new ResponseMessage(false, voteMessageProvider.getMessage("userDoNotHaveAnyVote"), comment);
        }

        comment.setDislikes(newDislikeVoteUsers);
        comment.setLikes(newLikeVoteUsers);

        return new ResponseMessage(true, voteMessageProvider.getMessage("notrVoted"), comment);
    }

    private boolean isCommentAndUserSafeForNull(Comment comment, User user) {
        return Objects.nonNull(comment) && Objects.nonNull(user);
    }

    private boolean isUserInList(List<VoteUser> voteUsers, User user) {
        boolean result = false;

        if (Objects.nonNull(voteUsers) && Objects.nonNull(user)) {
            result = voteUsers.stream().
                    anyMatch(voteUser -> Objects.nonNull(voteUser) &&
                            Objects.nonNull(voteUser.getUserId()) &&
                            voteUser.getUserId().equals(user.getId())
                    );
        }
        return result;
    }

    private boolean isCommentApproved(Comment comment) {
        return Objects.nonNull(comment) && Objects.nonNull(comment.getApproved()) &&
                comment.getApproved().equals("approved");
    }

    private boolean isCommentOwnedByUser(Comment comment, User user) {
        return Objects.nonNull(comment) && Objects.nonNull(comment.getUserId()) &&
                Objects.nonNull(user) && Objects.nonNull(user.getId()) &&
                comment.getUserId().equals(user.getId());
    }

    private List<VoteUser> removeFromList(List<VoteUser> voteUsers, User user) {
        List<VoteUser> oldVotes;

        if (Objects.nonNull(voteUsers) && Objects.nonNull(user)) {
            oldVotes = voteUsers.stream().filter(voteUser ->
                    !Objects.nonNull(voteUser) ||
                            !Objects.nonNull(voteUser.getUserId()) || !voteUser.getUserId().equals(user.getId())
            ).collect(Collectors.toList());

            return oldVotes;
        }

        return voteUsers;
    }

    private List<VoteUser> addToList(List<VoteUser> voteUsers, User user) {
        if (Objects.nonNull(voteUsers)) {
            if (Objects.nonNull(user)) {

                VoteUser voteUser = new VoteUser();
                voteUser.setUserId(user.getId());
                voteUser.setUsername(user.getUsername());
                voteUser.setFullname(user.getFullname());

                voteUsers.add(voteUser);
            }
        } else {
            voteUsers = new ArrayList<>();
        }

        return voteUsers;
    }
}
