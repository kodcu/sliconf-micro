package javaday.istanbul.sliconf.micro.service.star;

import javaday.istanbul.sliconf.micro.repository.StarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StarRepositoryService implements StarService {

    private Logger logger = LoggerFactory.getLogger(StarRepositoryService.class);

    @Autowired
    private StarRepository repo;

}