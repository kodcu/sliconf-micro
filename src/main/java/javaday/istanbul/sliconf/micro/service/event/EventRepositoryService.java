package javaday.istanbul.sliconf.micro.service.event;

import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.repository.EventRepository;
import javaday.istanbul.sliconf.micro.specs.EventSpecs;
import javaday.istanbul.sliconf.micro.specs.state.state.StateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Profile({"prod", "dev"})
public class EventRepositoryService implements EventService {

    protected Logger logger = LoggerFactory.getLogger(EventRepositoryService.class);

    @Autowired
    protected EventRepository repo;

    @Autowired
    protected EventStateService eventStateService;

    public Event findOne(String id) {
        return repo.findById(id);
    }

    public List<Event> findAll() {
        List<Event> events = new ArrayList<>();

        for (Event event : repo.findAll()) {
            events.add(event);
        }

        return events;
    }

    public List<Event> findByName(String name) {
        return repo.findByName(name);
    }

    public List<Event> findByNameAndDeleted(String name, Boolean deleted) {
        return repo.findByNameAndDeleted(name, deleted);
    }

    /**
     * Eventleri isim eslesiyorsa, key eslesmiyorsa ve deleted eslesiyorsa seklinde arar
     *
     * @param name
     * @param key
     * @param deleted
     * @return
     */
    public List<Event> findByNameAndNotKeyAndDeleted(String name, String key, Boolean deleted) {
        return repo.findByNameAndKeyNotAndDeleted(name, key, deleted);
    }

    public void delete(Event event) {
        repo.delete(event);
    }

    public Event findByKeyAndExecutiveUser(String key, String userId) {
        return repo.findByKeyAndExecutiveUser(key, userId);
    }

    public ResponseMessage save(Event event) {
        ResponseMessage message = new ResponseMessage(false, "An error occured while saving event", null);

        ResponseMessage eventStateMessage = StateManager.isEventCompatibleWithState(findOne(event.getId()), event, eventStateService);

        if (!eventStateMessage.isStatus()) {
            return eventStateMessage;
        }

        EventSpecs.generateStatusDetails(event);

        saveEvent(event, message);

        return message;
    }

    public ResponseMessage saveAdmin(Event event) {
        ResponseMessage message = new ResponseMessage(false, "An error occured while saving event", null);

        EventSpecs.generateStatusDetails(event);

        saveEvent(event, message);

        return message;
    }

    protected void saveEvent(Event event, ResponseMessage message) {
        try {
            Event savedEvent = repo.save(event);

            if (Objects.nonNull(savedEvent)) {
                message.setStatus(true);
                message.setMessage("Event saved successfully!");
                message.setReturnObject(savedEvent);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public Event findEventByKeyEquals(String key) {
        return repo.findEventByKeyEquals(key);
    }


    public List<Event> getNotDeletedEvents(List<Event> events) {
        if (Objects.nonNull(events)) {
            return events.stream()
                    .filter(event -> Objects.nonNull(event) && !event.getDeleted()).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<Event> getNotKeyEquals(List<Event> events, String key) {
        if (Objects.nonNull(events)) {
            return events.stream()
                    .filter(event -> Objects.nonNull(event) && Objects.nonNull(event.getKey()) &&
                            !event.getKey().equals(key))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public Map<String, List<Event>> findByExecutiveUser(String executiveUser) {
        Map<String, List<Event>> events = new HashMap<>();

        List<Event> eventList = repo.findAllByExecutiveUserEquals(executiveUser);

        eventList = getNotDeletedEvents(eventList);

        List<Event> activeList = new ArrayList<>();
        List<Event> passiveList = new ArrayList<>();
        final LocalDateTime now = LocalDateTime.now();

        if (Objects.nonNull(eventList)) {
            eventList.forEach(event -> {
                if (Objects.nonNull(event) && Objects.nonNull(event.getStartDate())) {
                    if (EventSpecs.checkIfEventDateAfterFromGivenDate(event, now)) {
                        activeList.add(event);
                    } else {
                        passiveList.add(event);
                    }
                }
            });
        }
        events.put("active", activeList);
        events.put("passive", passiveList);
        return events;
    }

    /**
     * Gelen keyle eslesen event var mi yok mu diye kontrol eder
     *
     * @param key event keyi
     * @return eger key ile event varsa true yoksa false doner
     */
    public boolean isKeyExists(String key) {
        Event event = repo.findEventByKeyEquals(key);
        return Objects.nonNull(event);
    }

    public void hideEventElements(Event event) {
        if (Objects.nonNull(event)) {
            event.setTotalUsers(null);
        }
    }

}