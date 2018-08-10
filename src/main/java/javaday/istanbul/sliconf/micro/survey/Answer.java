package javaday.istanbul.sliconf.micro.survey;


import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Answer {

    private String userId;
    Map<Integer, Integer> answers;
}
