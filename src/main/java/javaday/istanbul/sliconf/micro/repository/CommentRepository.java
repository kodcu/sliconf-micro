package javaday.istanbul.sliconf.micro.repository;

import javaday.istanbul.sliconf.micro.model.event.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentRepository extends CrudRepository<Comment, String> {

}