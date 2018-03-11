package javaday.istanbul.sliconf.micro.model;

import javaday.istanbul.sliconf.micro.model.event.Sponsor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SponsorsAndSponsorTags {
    private Map<String, List<Sponsor>> sponsors;
    private Map<String, String> sponsorTags;

}
