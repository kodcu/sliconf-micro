package javaday.istanbul.sliconf.micro.steps.survey;

import cucumber.api.java.tr.Fakat;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Ignore
public class AAA3 extends SpringBootTestConfig { // NOSONAR

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
        String exceptedErrorMessage = initialData.env.getProperty("survey.localDateTime.invalid");
        assertTrue(initialData.checkSurveyCreateErrorMessages(exceptedErrorMessage, initialData.survey));
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
        String exceptedErrorMessage = initialData.env.getProperty("survey.localDateTime.invalid");
        assertTrue(initialData.checkSurveyCreateErrorMessages(exceptedErrorMessage, initialData.survey));
    }

}
