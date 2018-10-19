package javaday.istanbul.sliconf.micro.steps.survey;

import cucumber.api.java.tr.Fakat;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.survey.service.SurveyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.assertTrue;

@Slf4j
@ContextConfiguration(classes = {CucumberConfiguration.class})
@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class AAA4 { // NOSONAR

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
        assertTrue(initialData.checkCreateSurveyErrorMessages(exceptedErrorMessage, initialData.survey));
    }
}
