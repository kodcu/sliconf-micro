package javaday.istanbul.sliconf.micro.agenda.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Konusma Elemani
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendaElement {
    private String id;

    private String topic;
    private String detail;

    private int level; // 0,1,2

    private List<String> tags;
    private String room;
    private String speaker;

    private double star;
    private long voteCount;
    private LocalDateTime date;

    private int duration;
}
