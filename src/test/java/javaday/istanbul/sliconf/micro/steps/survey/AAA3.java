package javaday.istanbul.sliconf.micro.steps.survey;

import cucumber.api.PendingException;
import cucumber.api.java.tr.Fakat;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.survey.service.SurveyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Slf4j
@ContextConfiguration(classes = {CucumberConfiguration.class})
@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class AAA3 {

    @Autowired
    SurveyService surveyService;
    @Autowired
    private InitialData initialData;

    @Fakat("^Anketin başlangıç tarihi uygun formatta değil ise$")
    public void anketinBaşlangıçTarihiUygunFormattaDeğilIse() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        initialData.survey.setStartTime("1567234a");
        assertFalse(initialData.survey.getStartTime().matches("^\\d+\\d$"));
    }

    @Ozaman("^Sistem anketi kayıt etmez ve ön tarafa hatali başlangıç tarihi gibi bir hata mesajı gönderilir$")
    public void sistemAnketiKayıtEtmezVeÖnTarafaHataliBaşlangıçTarihiGibiBirHataMesajıGönderilir() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        String exceptedErrorMessage = "Invalid localDateTime! ";
        assertTrue(initialData.checkErrorMessages(exceptedErrorMessage, initialData.survey));
    }

    @Fakat("^Anketin bitiş tarihi uygun formatta değil ise$")
    public void anketinBitişTarihiUygunFormattaDeğilIse() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        initialData.survey.setEndTime("112414$15");
        assertFalse(initialData.survey.getEndTime().matches("^\\d+\\d$"));
    }

    @Ozaman("^Sistem anketi kayıt etmez ve ön tarafa hatali bitiş tarihi gibi bir hata mesajı gönderilir$")
    public void sistemAnketiKayıtEtmezVeÖnTarafaHataliBitişTarihiGibiBirHataMesajıGönderilir() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        String exceptedErrorMessage = "Invalid localDateTime! ";
        assertTrue(initialData.checkErrorMessages(exceptedErrorMessage, initialData.survey));
    }

}
