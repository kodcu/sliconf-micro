package javaday.istanbul.sliconf.micro.specs;

import javaday.istanbul.sliconf.micro.model.event.agenda.AgendaElement;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class AgendaSpecs {

    @Autowired
    private static EventRepositoryService eventRepositoryService;

    /**
     * -1 -> Coffee Break
     */
    private static List<Integer> levelList = Arrays.asList(-1, 0, 1, 2);

    private AgendaSpecs() {
        // private constructor for static access
    }

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
        return Objects.nonNull(agendaElement) && checkIfLevelValid(agendaElement);
    }

    private static boolean checkIfLevelValid(AgendaElement agendaElement) {
        return Objects.nonNull(agendaElement.getLevel()) && levelList.contains(agendaElement.getLevel());
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

    public static List<AgendaElement> sortAgenda(List<AgendaElement> agenda) {
        // Agendayi tarihe gore sirala
        agenda.sort((agendaElement1, agendaElement2) -> {
            if (Objects.isNull(agendaElement1) || Objects.isNull(agendaElement2) ||
                    Objects.isNull(agendaElement1.getDate()) || Objects.isNull(agendaElement2.getDate())) {
                return 0;
            }

            if (agendaElement1.getDate().isBefore(agendaElement2.getDate())) {
                return -1;
            } else if (agendaElement1.getDate().isEqual(agendaElement2.getDate())) {
                return 0;
            }
            return 1;
        });

        return agenda;
    }

    public static AgendaElement getAgendaElement(List<AgendaElement> agendaElements, String sessionId) {
        AgendaElement returnAgendaElement = null;

        if (Objects.nonNull(agendaElements)) {
            for (AgendaElement agendaElement : agendaElements) {
                if (Objects.nonNull(agendaElement) && Objects.nonNull(agendaElement.getId())
                        && agendaElement.getId().equals(sessionId)) {
                    returnAgendaElement = agendaElement;
                    break;
                }
            }
        }

        return returnAgendaElement;
    }
}
