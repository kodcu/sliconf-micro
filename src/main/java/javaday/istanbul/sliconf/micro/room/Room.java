package javaday.istanbul.sliconf.micro.room;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Room {

    private String id;
    private String label;
    private String floor;
}
