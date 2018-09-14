package javaday.istanbul.sliconf.micro.steps.survey;

import cucumber.api.PendingException;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.survey.service.SurveyService;
import javaday.istanbul.sliconf.micro.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@Slf4j
@ContextConfiguration(classes = {CucumberConfiguration.class})
@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class AAA2{

    @Autowired
    private InitialData initialData;

    @Autowired
    SurveyService surveyService;

    @Eğerki("^Etkinlik sahibi anket ismini boş bırakmış ise$")
    public void etkinlikSahibiAnketIsminiBoşBırakmışIse() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        initialData.init();
        initialData.survey.setName(null);
        assertThat(initialData.survey.getName(), (isEmptyOrNullString()));

        initialData.survey.setName("");
        assertThat(initialData.survey.getName(), (isEmptyOrNullString()));
    }

    @Ozaman("^Sistem anketi kayıt etmez ve ön tarafa anket ismi boş olamaz gibi bir hata mesajı gönderilir$")
    public void sistemAnketiKayıtEtmezVeÖnTarafaAnketIsmiBoşOlamazGibiBirHataMesajıGönderilir() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        String exceptedErrorMessage = "Survey name can not be empty! ";
        assertTrue(initialData.checkErrorMessages(exceptedErrorMessage, initialData.survey));
    }
}
