package javaday.istanbul.sliconf.micro.steps.survey;

import cucumber.api.java.tr.Fakat;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@Ignore
public class AAA2 extends SpringBootTestConfig { // NOSONAR

    @Autowired
    InitialData initialData;

    @Fakat("^Etkinlik sahibi anket ismini boş bırakmış ise$")
    public void etkinlikSahibiAnketIsminiBoşBırakmışIse() throws Throwable {

        initialData.init();
        initialData.survey.setName(null);
        assertThat(initialData.survey.getName(), (isEmptyOrNullString()));

        initialData.survey.setName("");
        assertThat(initialData.survey.getName(), (isEmptyOrNullString()));
    }

    @Ozaman("^Sistem anketi kayıt etmez ve ön tarafa anket ismi boş olamaz gibi bir hata mesajı gönderilir$")
    public void sistemAnketiKayıtEtmezVeÖnTarafaAnketIsmiBoşOlamazGibiBirHataMesajıGönderilir() throws Throwable {

        String exceptedErrorMessage = initialData.env.getProperty("survey.name.blank");
        assertTrue(initialData.checkSurveyCreateErrorMessages(exceptedErrorMessage, initialData.survey));
    }
}
