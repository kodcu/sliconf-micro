package javaday.istanbul.sliconf.micro.specs.state.state;

import javaday.istanbul.sliconf.micro.model.event.BaseEventState;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.event.Room;
import javaday.istanbul.sliconf.micro.model.event.TotalUsers;
import javaday.istanbul.sliconf.micro.model.event.agenda.AgendaElement;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.event.EventStateService;

import java.util.List;
import java.util.Objects;

public class StateManager {

    private StateManager() {
        // Private for static access
    }

    public static ResponseMessage isEventCompatibleWithState(Event eventFromDB, Event newEvent, EventStateService eventStateService) {
        BaseEventState baseEventState = null;

        if (Objects.nonNull(eventFromDB) && Objects.nonNull(eventFromDB.getEventState())) {
            baseEventState = eventStateService.findOne(eventFromDB.getEventState().getId());
        }

        if (Objects.isNull(baseEventState)) {
            baseEventState = eventStateService.findByType("default");
        }

        if (Objects.isNull(newEvent)) {
            return new ResponseMessage(false, "Event can not be null!", "");
        }

        newEvent.setEventState(baseEventState);

        if (Objects.isNull(baseEventState)) {
            return new ResponseMessage(false, "There is no event state for this event, please talk to an authorized person!", "");
        }

        if (!isRoomCountValid(newEvent.getRooms(), baseEventState.getRoomCount())) {
            return new ResponseMessage(false, "Room voteCount must be equal or below at " + baseEventState.getRoomCount(), "");
        }

        if (!isSessionCountValid(newEvent.getAgenda(), baseEventState.getSessionCount())) {
            return new ResponseMessage(false, "Session voteCount must be equal or below at " + baseEventState.getSessionCount(), "");
        }

        if (!isParticipiantCountValid(newEvent.getTotalUsers(), baseEventState.getParticipantCount())) {
            return new ResponseMessage(false, "Participiant voteCount must be equal or below at " + baseEventState.getParticipantCount(), "");
        }

        return new ResponseMessage(true, "Event state is valid", "");
    }

    private static boolean isRoomCountValid(List<Room> roomList, long stateRoomCount) {
        if (Objects.isNull(roomList)) {
            return true;
        }

        return roomList.size() <= stateRoomCount;
    }

    private static boolean isSessionCountValid(List<AgendaElement> agendaElements, long stateSessionCount) {
        if (Objects.isNull(agendaElements)) {
            return true;
        }

        return agendaElements.size() <= stateSessionCount;
    }

    private static boolean isParticipiantCountValid(TotalUsers totalUsers, long stateParticipiantCount) {
        if (Objects.isNull(totalUsers)) {
            return true;
        }

        return totalUsers.getUniqueCount() <= stateParticipiantCount;
    }
}
