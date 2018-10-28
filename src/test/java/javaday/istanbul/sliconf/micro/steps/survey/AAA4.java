package javaday.istanbul.sliconf.micro.steps.survey;

import cucumber.api.java.tr.Fakat;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.assertTrue;

@Ignore
public class AAA4 extends SpringBootTestConfig { // NOSONAR

    @Autowired
    private InitialData initialData;

    @Fakat("^Anketin başlangıç tarihi bugün veya daha ileri bir tarih olarak belirtilmemiş ise$")
    public void anketinBaşlangıçTarihiBugünVeyaDahaIleriBirTarihOlarakBelirtilmemişIse() throws Throwable {

        LocalDateTime startTime = LocalDateTime.now().minusDays(1);

        String invalidStartTime = Long.toString(startTime.toEpochSecond(ZoneOffset.UTC));
        initialData.survey.setStartTime(invalidStartTime);

        assertTrue(startTime.isBefore(LocalDateTime.now()));
    }

    @Ozaman("^Sistem anketi kayıt etmez ve ön tarafa başlangıç tarihi yanlış gibi bir hata mesajı gönderilir$")
    public void sistemAnketiKayıtEtmezVeÖnTarafaBirHataMesajıGönderilir() throws Throwable {

        String exceptedErrorMessage = initialData.env.getProperty("survey.startTime-before-now");
        assertTrue(initialData.checkSurveyCreateErrorMessages(exceptedErrorMessage, initialData.survey));
    }
}
