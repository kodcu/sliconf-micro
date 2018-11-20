package javaday.istanbul.sliconf.micro.sponsor;

import com.devskiller.jfairy.Fairy;
import com.google.common.collect.Lists;
import javaday.istanbul.sliconf.micro.event.model.Event;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SponsorGenerator {

    private SponsorGenerator() {}

    public static void generateRandomSponsors(int count, Event event) {

        event.setSponsorTags(generateSponsorTags());
        Fairy fairy = Fairy.create();

        Map<String, List<Sponsor>> sponsorMap = new HashMap<>();
        List<List<Sponsor>>  lists = new ArrayList<>();

        int sponsorTypeCount = 5;

        for (int i = 0; i < sponsorTypeCount; i++) {
            lists.add(new ArrayList<>());
        }
        for (int i = 0; i < count; i++) {
            Sponsor sponsor = Sponsor.builder()
                    .id(new ObjectId().toString())
                    .name(fairy.company().getName())
                    .build();
            lists.get(i % sponsorTypeCount).add(sponsor);
        }
        for (List<Sponsor> sponsorList: lists) {

            String sponsorTagId = fairy.baseProducer().randomElement(Lists.newArrayList(event.getSponsorTags().keySet()));
            sponsorMap.put(sponsorTagId, sponsorList);

            // bir sirket hem gold hem silver sponsor olmasin diye :)
            event.getSponsorTags().remove(sponsorTagId);
        }

        event.setSponsorTags(generateSponsorTags());
        event.setSponsors(sponsorMap);

    }

    private static Map<String, String> generateSponsorTags() {

        Map<String, String> sponsorTagMap = new HashMap<>();

        sponsorTagMap.put(new ObjectId().toString(), "Bronze");
        sponsorTagMap.put(new ObjectId().toString(), "Silver");
        sponsorTagMap.put(new ObjectId().toString(), "Gold");
        sponsorTagMap.put(new ObjectId().toString(), "Platinum");
        sponsorTagMap.put(new ObjectId().toString(), "Diamond");

        return sponsorTagMap;

    }
}
