package javaday.istanbul.sliconf.micro.comment;

import javaday.istanbul.sliconf.micro.comment.model.ModerateCommentModel;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.comment.model.Comment;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.event.service.EventRepositoryService;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CommentSpecs {

    private static List<String> commentListTypes = Arrays.asList("top-rated", "recent", "oldest");

    private CommentSpecs() {
        // private constructor for static
    }

    /**
     * Gelen comment'in null olup olmadigi ve alanlarinin bos olup olmadigi kontrolu
     *
     * @param comment
     * @return
     */
    public static boolean isAllFieldsAreSafeForProcess(Comment comment) {
        return Objects.nonNull(comment) &&
                Objects.nonNull(comment.getCommentValue()) &&
                Objects.nonNull(comment.getEventId()) &&
                Objects.nonNull(comment.getSessionId()) &&
                Objects.nonNull(comment.getTime()) &&
                Objects.nonNull(comment.getUserId());
    }

    /**
     * Yonetilecek commentleri barindiran model nesnesi duzgun mu diye kontrol ediliyor
     *
     * @param model
     * @return
     */
    public static boolean isModerateCommentModelSafeForProcess(ModerateCommentModel model) {
        return Objects.nonNull(model) &&
                Objects.nonNull(model.getEventId()) &&
                Objects.nonNull(model.getUserId());
    }

    /**
     * Comment listelerken gelen tipe gore bir listeleme yapiliyor.
     * Bu tipin gecerli bir tip olup olmadiginin bir kontrolu
     *
     * @param type
     * @return
     */
    public static boolean isCommentTypeValid(String type) {
        return Objects.nonNull(type) && commentListTypes.contains(type);
    }

    public static ResponseMessage checkIfUserExists(String userId, UserRepositoryService userRepositoryService, CommentMessageProvider commentMessageProvider) {
        User user = userRepositoryService.findById(userId).orElse(null);

        if (Objects.nonNull(user)) {
            return new ResponseMessage(true, commentMessageProvider.getMessage("userFoundWithGivenId"), user);
        } else {
            return new ResponseMessage(false, commentMessageProvider.getMessage("userCanNotFoundWithGivenId"), userId);
        }
    }

    public static ResponseMessage checkIfEventExists(String eventId, EventRepositoryService eventRepositoryService, CommentMessageProvider commentMessageProvider) {
        Event event = eventRepositoryService.findOne(eventId);

        if (Objects.nonNull(event)) {
            return new ResponseMessage(true, commentMessageProvider.getMessage("eventFoundWithGivenId"), event);
        } else {
            return new ResponseMessage(false, commentMessageProvider.getMessage("eventCanNotFoundWithGivenId"), eventId);
        }
    }
}
