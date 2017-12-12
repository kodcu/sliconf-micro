package javaday.istanbul.sliconf.micro.specs;

import javaday.istanbul.sliconf.micro.model.event.Comment;

import java.util.Objects;

public class CommentSpecs {
    private CommentSpecs() {
        // private constructor for static
    }

    /**
     * Gelen comment'in null olup olmadigi ve alanlarinin bos olup olmadigi kontrolu
     * @param comment
     * @return
     */
    public static boolean isAllFieldsAreSafeForProcess(Comment comment) {
        if (Objects.isNull(comment) ||
                Objects.isNull(comment.getCommentValue()) ||
                Objects.isNull(comment.getEventId()) ||
                Objects.isNull(comment.getSessionId()) ||
                Objects.isNull(comment.getTime()) ||
                Objects.isNull(comment.getUserId())) {
            return false;
        }

        return true;
    }
}
