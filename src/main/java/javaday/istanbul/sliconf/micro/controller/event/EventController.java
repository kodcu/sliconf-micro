package javaday.istanbul.sliconf.micro.controller.event;

import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.EventControllerMessageProvider;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.specs.EventSpecs;
import javaday.istanbul.sliconf.micro.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class EventController {

    @Autowired
    private EventControllerMessageProvider messageProvider;

    @Autowired
    private EventRepositoryService repositoryService;

    public ResponseMessage createEvent(Request request, Response response) {
        ResponseMessage responseMessage;

        String body = request.body();

        String userId = request.queryParams(":userId");

        if (Objects.isNull(userId)) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventUserIdCantBeEmpty"), new Object());
            return responseMessage;
        }

        if(Objects.isNull(body) || body.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventBodyCantBeEmpty"), new Object());
            return responseMessage;
        }

        Event event = JsonUtil.fromJson(body, Event.class);

        //isim uzunluğu minimumdan düşük mü diye kontrol et
        if(EventSpecs.checkEventName(event, 4)){
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventNameTooShort"), new Object());
            return responseMessage;
        }

        //event tarihinin geçip geçmediğin, kontrol et
        if(!EventSpecs.checkIfEventDateAfterOrInNow(event)){
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventDataInvalid"), new Object());
            return responseMessage;
        }

        // event var mı diye kontrol et
        List<Event> dbEvents = repositoryService.findByName(event.getName());

        if (Objects.nonNull(dbEvents) && !dbEvents.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventAlreadyRegistered"), new Object());
            return responseMessage;
        }

        //Kanban numarası oluştur
        EventSpecs.generateKanbanNumber(event);

        event.setExecutiveUser(userId);

        // eger event yoksa kayit et
        ResponseMessage dbResponse = repositoryService.save(event);

        if (!dbResponse.isStatus()) {
            return dbResponse;
        }

        responseMessage = new ResponseMessage(true,
                messageProvider.getMessage("eventCreatedSuccessfully"), event);

        return responseMessage;
    }

    public ResponseMessage getEventWithKey(Request request, Response response) {
        ResponseMessage responseMessage;

        String key = request.queryParams(":key");

        // event var mı diye kontrol et
        Event event = repositoryService.findEventByKeyEquals(key);

        if (Objects.isNull(event)) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventCanNotFound"), new Object());
            return responseMessage;
        }

        responseMessage = new ResponseMessage(true,
                messageProvider.getMessage("eventCreatedSuccessfully"), event);

        return responseMessage;
    }

    public ResponseMessage listEvents(Request request, Response response) {
        ResponseMessage responseMessage;

        String userId = request.queryParams(":userId");

        // event var mı diye kontrol et
        Map<String, Event> events = repositoryService.findByExecutiveUser(userId);

        if (Objects.isNull(events)) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventCanNotFound"), new Object());
            return responseMessage;
        }

        responseMessage = new ResponseMessage(true,
                messageProvider.getMessage("eventCreatedSuccessfully"), events);

        return responseMessage;
    }
}
