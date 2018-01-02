package javaday.istanbul.sliconf.micro.specs;

import javaday.istanbul.sliconf.micro.model.ModerateCommentModel;
import javaday.istanbul.sliconf.micro.model.event.Comment;

import java.util.Objects;

public class CommentSpecs {
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
}
