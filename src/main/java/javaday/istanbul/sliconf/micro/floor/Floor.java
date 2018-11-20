package javaday.istanbul.sliconf.micro.floor;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Floor {

    private String id;
    private String name;
    private String image;
}
