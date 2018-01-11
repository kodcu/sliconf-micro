package javaday.istanbul.sliconf.micro.model.event;

import java.util.List;

public class TotalUsers {
    private long allFetched;
    private int uniqueCount;
    private List<TotalUser> users;

    public long getAllFetched() {
        return allFetched;
    }

    public void setAllFetched(long allFetched) {
        this.allFetched = allFetched;
    }

    public int getUniqueCount() {
        return uniqueCount;
    }

    public void setUniqueCount(int uniqueCount) {
        this.uniqueCount = uniqueCount;
    }

    public List<TotalUser> getUsers() {
        return users;
    }

    public void setUsers(List<TotalUser> users) {
        this.users = users;
    }
}
