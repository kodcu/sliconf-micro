package javaday.istanbul.sliconf.micro.model;

import javaday.istanbul.sliconf.micro.model.event.Sponsor;

import java.util.List;
import java.util.Map;

public class SponsorsAndSponsorTags {
    private Map<String, List<Sponsor>> sponsors;
    private Map<String, String> sponsorTags;

    public Map<String, List<Sponsor>> getSponsors() {
        return sponsors;
    }

    public void setSponsors(Map<String, List<Sponsor>> sponsors) {
        this.sponsors = sponsors;
    }

    public Map<String, String> getSponsorTags() {
        return sponsorTags;
    }

    public void setSponsorTags(Map<String, String> sponsorTags) {
        this.sponsorTags = sponsorTags;
    }
}
