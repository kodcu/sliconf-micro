package javaday.istanbul.sliconf.micro.model.event;

import java.util.List;
import java.util.Map;

public class About {
    private Map<String, String> social;

    private String web;

    private List<String> phone;

    private Location location;

    public Map<String, String> getSocial() {
        return social;
    }

    public void setSocial(Map<String, String> social) {
        this.social = social;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
