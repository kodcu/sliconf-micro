package javaday.istanbul.sliconf.micro.event.service;

import com.google.api.client.util.Lists;
import javaday.istanbul.sliconf.micro.event.EventSpecs;
import javaday.istanbul.sliconf.micro.event.StateManager;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.event.model.EventFilter;
import javaday.istanbul.sliconf.micro.event.model.LifeCycleState;
import javaday.istanbul.sliconf.micro.event.repository.EventRepository;
import javaday.istanbul.sliconf.micro.mail.IMailSendService;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Profile({"prod", "dev"})
public class EventRepositoryService implements EventService {

    @Value("${spring.profiles.active}")
    String activeProfile;

    protected Logger logger = LoggerFactory.getLogger(EventRepositoryService.class);

    @Autowired
    protected EventRepository repo;

    @Autowired
    protected EventStateService eventStateService;

    @Autowired
    @Qualifier("gandiMailSendService")
    private IMailSendService mailSendService;

    public Optional<Event> findById(String id) {
        return repo.findById(id);
    }

    public Event findOne(String id) {
        return repo.findOne(id);
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
        ResponseMessage message = new ResponseMessage(false, "An error occurred while saving event", null);

        // TODO: 02.10.2018 update ve create ayni yerden yapildigi icin bir takim sikintilar mevcut olabiliyor. bunlari ayirmali. #1158
        Event dbEvent = this.findById(event.getId()).orElse(null);
        ResponseMessage eventStateMessage = StateManager.isEventCompatibleWithState(dbEvent, event, eventStateService);
        if (!eventStateMessage.isStatus()) {
            return eventStateMessage;
        }

        EventSpecs.generateStatusDetails(event, mailSendService);


        saveEvent(event, message);

        return message;
    }

    public ResponseMessage saveAdmin(Event event) {
        ResponseMessage message = new ResponseMessage(false, "An error occured while saving event", null);

        EventSpecs.generateStatusDetails(event, mailSendService);

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

    @Override
    public Optional<Event> findByKey(String eventKey) {
        return repo.findByKey(eventKey);
    }


    public Page<Event> filter(EventFilter eventFilter, Pageable pageable) {
        List<LifeCycleState.EventStatus> eventStatuses;
        if (eventFilter.getEventStatuses().isEmpty()) {
            eventFilter.getEventStatuses().add("ACTIVE");
            eventFilter.getEventStatuses().add("HAPPENING");

        }
        eventStatuses = eventFilter.getEventStatuses()
                .stream()
                .map(LifeCycleState.EventStatus::valueOf)
                .collect(Collectors.toList());

        // Query sorgulari test veritabani uzerinde calismiyor. o yuzden sadece prod ve devde calistiriyoruz.
        if (!activeProfile.equals("test")) {
            Page<Event> result = repo.findAllByLifeCycleStateEventStatusesAndNameLike(eventStatuses, eventFilter.getNameLike(), pageable);

            if (result !=null) {
                logger.debug("total result " + result.getSize());
                System.out.println("total result " + result.getSize());
            }

            // sort according to dates. closest event should be at top
            List<Event> sortableList = new ArrayList<>(result.getContent());
            Collections.sort(sortableList, Comparator.comparing(Event::getStartDate)); // defaul asc mode

            // testing this part - https://redmine.kodcu.com/issues/1489
            if(eventFilter.getSort()!= null && eventFilter.getSort().equalsIgnoreCase("desc") ) {

                //  01 April, 23 march  ...
                Collections.sort(sortableList, Comparator.comparing(Event::getStartDate).reversed());


            }

            Page<Event> pages = new PageImpl<Event>(sortableList, pageable, sortableList.size());
            return pages;


        }

        Set<Event> events = new HashSet<>();
        eventStatuses.stream().map(repo::findAllByLifeCycleStateEventStatusesLike).forEach(events::addAll);

        return new PageImpl<>(Lists.newArrayList(events), pageable, events.size());
    }
}