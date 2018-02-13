package javaday.istanbul.sliconf.micro.model.response;

public class ListCommentResponseMessage extends ResponseMessage {
    private String commentType;

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }
}
