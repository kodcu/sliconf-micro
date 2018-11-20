package javaday.istanbul.sliconf.micro.event.service;

import javaday.istanbul.sliconf.micro.event.model.BaseEventState;
import javaday.istanbul.sliconf.micro.event.repository.EventStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventStateService {

    protected Logger logger = LoggerFactory.getLogger(EventStateService.class);

    @Autowired
    protected EventStateRepository repo;

    public BaseEventState findOne(String id) {
        return repo.findById(id);
    }

    public List<BaseEventState> findAll() {
        return repo.findAll();
    }

    public BaseEventState findByName(String name) {
        return repo.findByName(name);
    }

    public BaseEventState findByType(String type) {
        return repo.findByType(type);
    }
}