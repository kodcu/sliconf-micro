package javaday.istanbul.sliconf.micro.specs;

import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;

public class UserSpecs {

    public static boolean checkUserParams(String param, int nameLength) {
        return param.length() >= nameLength;
    }
}
