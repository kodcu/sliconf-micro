package javaday.istanbul.sliconf.micro.steps.survey;

import cucumber.api.java.tr.Eğerki;
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

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@Slf4j
@ContextConfiguration(classes = {CucumberConfiguration.class})
@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class AAA2 { // NOSONAR

    @Autowired
    InitialData initialData;

    @Fakat("^Etkinlik sahibi anket ismini boş bırakmış ise$")
    public void etkinlikSahibiAnketIsminiBoşBırakmışIse() throws Throwable {

        initialData.init();
        initialData.survey.setName(null);
        assertThat( initialData.survey.getName(), (isEmptyOrNullString()));

        initialData.survey.setName("");
        assertThat( initialData.survey.getName(), (isEmptyOrNullString()));
    }

    @Ozaman("^Sistem anketi kayıt etmez ve ön tarafa anket ismi boş olamaz gibi bir hata mesajı gönderilir$")
    public void sistemAnketiKayıtEtmezVeÖnTarafaAnketIsmiBoşOlamazGibiBirHataMesajıGönderilir() throws Throwable {

        String exceptedErrorMessage =  initialData.env.getProperty("survey.name.blank");
        assertTrue(initialData.checkCreateSurveyErrorMessages(exceptedErrorMessage,  initialData.survey));
    }
}
