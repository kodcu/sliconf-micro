package javaday.istanbul.sliconf.micro.specs;

import javaday.istanbul.sliconf.micro.model.event.agenda.AgendaElement;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class AgendaSpecs {

    @Autowired
    private static EventRepositoryService eventRepositoryService;


    public static ResponseMessage isAgendaValid(List<AgendaElement> agenda) {
        ResponseMessage responseMessage = new ResponseMessage();

        if (Objects.isNull(agenda)) {
            responseMessage.setStatus(false);
            responseMessage.setReturnObject("");
            responseMessage.setMessage("Agenda can not be null!");
            return responseMessage;
        }

        for (AgendaElement agendaElement : agenda) {

            if (!isAgendaElementValid(agendaElement)) {
                responseMessage.setStatus(false);
                responseMessage.setMessage("AgendaElement is not valid!");
                responseMessage.setReturnObject(agenda);

                return responseMessage;
            }
        }

        generateIds(agenda);

        responseMessage.setStatus(true);
        responseMessage.setMessage("Agenda is valid");
        responseMessage.setReturnObject(agenda);

        return responseMessage;
    }

    private static boolean isAgendaElementValid(AgendaElement agendaElement) {

        // Todo make this control more specific
        if (Objects.isNull(agendaElement)) {
            return false;
        }

        //eventRepositoryService.findAll();
        agendaElement.getRoom();
        agendaElement.getSpeaker();

        return true;
    }

    private static void checkIfRoomValid(String roomId) {




    }

    private static void generateIds(List<AgendaElement> agenda) {

        for (AgendaElement agendaElement : agenda) {
            if (Objects.nonNull(agendaElement) &&
                    (Objects.isNull(agendaElement.getId()) ||
                            agendaElement.getId().isEmpty() ||
                            agendaElement.getId().contains("newid"))) {

                String newId = UUID.randomUUID().toString();
                agendaElement.setId(newId);
            }
        }
    }

}
