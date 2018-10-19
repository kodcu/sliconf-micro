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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Slf4j
@ContextConfiguration(classes = {CucumberConfiguration.class})
@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class AAA5 { // NOSONAR

    @Autowired
    private InitialData initialData;

    @Fakat("^Anketin bitiş tarihi başlangıç tarihinden önce ise$")
    public void anketinBitişTarihiBaşlangıçTarihindenÖnceIse() throws Throwable {
        LocalDateTime startTime = LocalDateTime.now().plusDays(5);
        LocalDateTime endtime = LocalDateTime.now().plusDays(3);
        String invalidStartTime = Long.toString(startTime.toEpochSecond(ZoneOffset.UTC));
        String invalidEndTime = Long.toString(endtime.toEpochSecond(ZoneOffset.UTC));

        initialData.survey.setStartTime(invalidStartTime);
        initialData.survey.setEndTime(invalidEndTime);
        assertFalse(startTime.isBefore(endtime));

    }

    @Ozaman("^Sistem anketi kayıt etmez ve ön tarafa hatalı başlangıç-bitiş tarihi gibi bir hata mesajı gönderilir$")
    public void sistemAnketiKayıtEtmezVeÖnTarafaHatalıBaşlangıçBitişTarihiGibiBirHataMesajıGönderilir() throws Throwable {
        String exceptedErrorMessage = initialData.env.getProperty("survey.startAndEndTime.invalid");
        assertTrue(initialData.checkCreateSurveyErrorMessages(exceptedErrorMessage, initialData.survey));


    }
}
