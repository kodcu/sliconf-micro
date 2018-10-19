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

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@Slf4j
@ContextConfiguration(classes = {CucumberConfiguration.class})
@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class AAA9 { // NOSONAR

    @Autowired
    private InitialData initialData;

    @Fakat("^Anketteki soru şıklarının isimleri geçerli değil ise$")
    public void ankettekiSoruŞıklarınınIsimleriGeçerliDeğilIse() throws Throwable {

        initialData.survey.getQuestions()
                .forEach(question -> question.getOptions()
                        .forEach(questionOption -> questionOption.setText("")));

        initialData.survey.getQuestions()
                .forEach(question -> question.getOptions()
                        .forEach(questionOption ->
                                assertThat(questionOption.getText(), isEmptyOrNullString())));
    }

    @Ozaman("^Sistem anketi kayıt etmez ve ön tarafa şıkların isimleri boş gibi bir hata mesajı gönderilir$")
    public void sistemAnketiKayıtEtmezVeÖnTarafaŞıklarınIsimleriBoşGibiBirHataMesajıGönderilir() throws Throwable {
        String exceptedErrorMessage = initialData.env.getProperty("survey.question.questionOptions.text.blank");
        assertTrue(initialData.checkCreateSurveyErrorMessages(exceptedErrorMessage, initialData.survey));
    }
}
