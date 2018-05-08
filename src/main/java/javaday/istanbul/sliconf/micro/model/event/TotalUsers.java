package javaday.istanbul.sliconf.micro.model.event;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Event uzerinde total users istatistigi icin tutulan nesne
 */
@Getter
@Setter
public class TotalUsers {
    private long allFetched;
    private int uniqueCount;
    private List<TotalUser> users;
}
