package javaday.istanbul.sliconf.micro.comment.model;

import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import lombok.Getter;
import lombok.Setter;

/**
 * Web tarafinda yorumlar onaylanirken, yorumun tipinin donmesi icin kullanilan child nesne
 */
@Getter
@Setter
public class ListCommentResponseMessage extends ResponseMessage {
    private String commentType;

}
