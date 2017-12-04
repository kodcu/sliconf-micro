package javaday.istanbul.sliconf.micro.specs;

import javaday.istanbul.sliconf.micro.model.event.Speaker;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.util.Constants;

import java.util.List;
import java.util.Objects;


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
        ResponseMessage responseMessage = new ResponseMessage(true, "Speaker is valid","");

        if (Objects.isNull(speaker)) {
            responseMessage.setStatus(false);
            responseMessage.setMessage("Speaker can not be null!");
            responseMessage.setReturnObject("");
            return responseMessage;
        }

        return responseMessage;
    }
}
