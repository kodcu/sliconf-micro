package javaday.istanbul.sliconf.micro.steps.survey;

import cucumber.api.java.tr.Fakat;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.survey.service.SurveyService;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@Slf4j
@ContextConfiguration(classes = {CucumberConfiguration.class})
@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class AAA8 { // NOSONAR

    @Autowired
    private InitialData initialData;

    @Fakat("^Anketteki sorular en az iki şık içermiyor ise$")
    public void ankettekiSorularEnAzIkiŞıkIçermiyorIse() throws Throwable {
        initialData.survey.getQuestions().forEach(question -> question.setOptions(new ArrayList<>()));
        initialData.survey.getQuestions()
                .forEach(question ->
                        assertThat(question.getOptions().size(), Matchers.lessThanOrEqualTo(1)));

    }

    @Ozaman("^Sistem anketi kayıt etmez ve ön tarafa sorular en az 2 şık içermeli gibi bir hata mesajı gönderilir$")
    public void sistemAnketiKayıtEtmezVeÖnTarafaSorularEnAz2ŞıkİçermeliGibiBirHataMesajıGönderilir() throws Throwable {
        String exceptedErrorMessage = initialData.env.getProperty("survey.question.questionOptions.size");
        assertTrue(initialData.checkCreateSurveyErrorMessages(exceptedErrorMessage, initialData.survey));
    }
}
