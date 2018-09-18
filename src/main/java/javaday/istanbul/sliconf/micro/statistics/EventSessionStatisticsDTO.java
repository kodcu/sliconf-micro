package javaday.istanbul.sliconf.micro.statistics;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class EventSessionStatisticsDTO {

    private String photo;
    private String speaker;
    private String workingAt;
    private String topic;
    private String count;
    private String average;

}
