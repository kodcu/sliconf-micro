package javaday.istanbul.sliconf.micro.speaker;

import javaday.istanbul.sliconf.micro.agenda.model.AgendaElement;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.util.Constants;
import javaday.istanbul.sliconf.micro.util.SpeakerComparator;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class SpeakerSpecs {

    private SpeakerSpecs() {
        // Static yapi icin gizli yapici yordam
    }

    public static ResponseMessage isSpekarsValid(List<Speaker> speakers) {
        ResponseMessage responseMessage = new ResponseMessage(false, "Speakers are not valid!", "");

        if (Objects.isNull(speakers)) {
            responseMessage.setMessage("Speaker list can not be null!");
            return responseMessage;
        }

        for (Speaker speaker : speakers) {
            responseMessage = isSpeakerValid(speaker);

            if (!responseMessage.isStatus()) {
                return responseMessage;
            }
        }

        responseMessage.setStatus(true);
        responseMessage.setMessage("Speakers are valid");
        responseMessage.setReturnObject(speakers);

        return responseMessage;
    }

    public static ResponseMessage isSpeakerValid(Speaker speaker) {
        ResponseMessage responseMessage = new ResponseMessage(true, "Speaker is valid", "");

        if (Objects.isNull(speaker)) {
            responseMessage.setStatus(false);
            responseMessage.setMessage("Speaker can not be null!");
            responseMessage.setReturnObject("");
            return responseMessage;
        }

        return responseMessage;
    }

    public static void generateSpeakerIds(List<Speaker> speakers) {
        if (Objects.nonNull(speakers)) {
            for (Speaker speaker : speakers) {
                if (Objects.nonNull(speaker) &&
                        (Objects.isNull(speaker.getId()) ||
                                speaker.getId().contains("newid") ||
                                "".equals(speaker.getId()))) {
                    speaker.setId(UUID.randomUUID().toString());
                }
            }
        }
    }

    public static void sortSpeakersByName(List<Speaker> speakers) {
        if (Objects.nonNull(speakers)) {
            speakers.sort(new SpeakerComparator());
        }
    }

    /**
     * Silinen speakerlara bagli agenda elementleri sil
     *
     * @param event
     */
    public static void removeAgendaElementsWithNoSpeaker(Event event) {
        if (Objects.nonNull(event) &&
                Objects.nonNull(event.getSpeakers()) &&
                Objects.nonNull(event.getAgenda())) {

            List<AgendaElement> agenda = event.getAgenda();
            List<Speaker> speakers = event.getSpeakers();

            List<AgendaElement> agendaElements = agenda.stream()
                    .filter(agendaElement -> {
                        if (isBreakElement(agendaElement)) {
                            return true;
                        } else {
                            return speakers.stream().anyMatch(isSpeakerExistsWithAgenda(agendaElement));
                        }
                    }).collect(Collectors.toList());

            event.setAgenda(agendaElements);
        }
    }

    /**
     * Agenda elemaninin coffee break gibi bir eleman olup olmadiginin kontrolu
     *
     * @param agendaElement
     * @return
     */
    private static boolean isBreakElement(AgendaElement agendaElement) {
        return Objects.nonNull(agendaElement) && agendaElement.getLevel() == Constants.Agenda.BREAK;
    }

    private static Predicate<Speaker> isSpeakerExistsWithAgenda(AgendaElement agendaElement) {
        return speaker -> Objects.nonNull(speaker) && Objects.nonNull(speaker.getId()) &&
                Objects.nonNull(agendaElement) && Objects.nonNull(agendaElement.getId()) &&
                speaker.getId().equals(agendaElement.getSpeaker());
    }

    public static Speaker getSpeaker(List<Speaker> speakers, String speakerId) {
        Speaker returnSpeaker = null;

        if (Objects.nonNull(speakers)) {
            for (Speaker speaker : speakers) {
                if (Objects.nonNull(speaker) && Objects.nonNull(speaker.getId())
                        && speaker.getId().equals(speakerId)) {
                    returnSpeaker = speaker;
                    break;
                }
            }
        }

        return returnSpeaker;
    }
}
