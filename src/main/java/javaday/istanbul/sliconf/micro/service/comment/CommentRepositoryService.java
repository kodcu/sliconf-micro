package javaday.istanbul.sliconf.micro.service.comment;

import javaday.istanbul.sliconf.micro.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentRepositoryService implements CommentService {

    private Logger logger = LoggerFactory.getLogger(CommentRepositoryService.class);

    @Autowired
    private CommentRepository repo;

}