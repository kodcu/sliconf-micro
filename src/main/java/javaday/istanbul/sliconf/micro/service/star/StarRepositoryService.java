package javaday.istanbul.sliconf.micro.service.star;

import javaday.istanbul.sliconf.micro.model.event.agenda.Star;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.repository.StarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class StarRepositoryService implements StarService {

    @Autowired
    private StarRepository repository;

    public List<Star> findAllByEventIdAndSessionIdAndUserId(String eventId, String sessionId, String userId) {
        List<Star> starList = repository.findAllByEventIdAndSessionIdAndUserId(eventId, sessionId, userId);

        return Objects.nonNull(starList) ? starList : new ArrayList<>();
    }

    public ResponseMessage saveStar(Star star) {
        Star dbStar = repository.save(star);

        if (Objects.nonNull(dbStar)) {
            return new ResponseMessage(true, "Star saved successfully", dbStar);
        }
        return new ResponseMessage(false, "An error occured while saving star", star);
    }
}