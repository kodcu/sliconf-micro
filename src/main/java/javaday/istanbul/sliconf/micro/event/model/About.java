package javaday.istanbul.sliconf.micro.event.model;

import javaday.istanbul.sliconf.micro.event.model.Location;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Etkinlik hakkinda genel bilgilerin bulundugu nesne
 */
@Getter
@Setter
@Builder
public class About {
    private Map<String, String> social;

    private String web;

    private List<String> phone;

    private Location location;

    private String email;

}
