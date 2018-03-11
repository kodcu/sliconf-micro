package javaday.istanbul.sliconf.micro.model.event;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Event'in durumunu (kullanilabilir olma) belirten sinif.
 * Hangi bilgiler eksik hangi bilgiler girilmis. Event'in mobilden kullanimi icin
 * hangi bilgilerin girilmesi gerektigini gosteren bilgi
 */
@Getter
@Setter
public class StatusDetails {

    private int percentage;
    private List<String> passed;
    private List<String> failed;
    private List<String> optionalPassed;
    private List<String> optionalFailed;
}
