package javaday.istanbul.sliconf.micro.util;

import javaday.istanbul.sliconf.micro.speaker.Speaker;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

public class SpeakerComparator implements Comparator<Speaker> {

    @Override
    public int compare(Speaker speaker1, Speaker speaker2) {
        Collator collator = Collator.getInstance(new Locale("tr", "TR"));

        if (Objects.isNull(speaker1) || Objects.isNull(speaker2) ||
                Objects.isNull(speaker1.getName()) || Objects.isNull(speaker2.getName())) {
            return 0;
        }

        return collator.compare(speaker1.getName(), speaker2.getName());
    }
}
