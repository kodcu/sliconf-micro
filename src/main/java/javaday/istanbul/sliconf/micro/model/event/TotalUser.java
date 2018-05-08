package javaday.istanbul.sliconf.micro.model.event;

import lombok.Getter;
import lombok.Setter;

/**
 * Event istatistik asamasinda event e erisim saglayan kullaniciyi belirten nesne
 */
@Getter
@Setter
public class TotalUser {

    private String id;
    private String username;
    private String email;
    private String fullname;
    private Boolean anonymous;
    private String deviceId;
}
