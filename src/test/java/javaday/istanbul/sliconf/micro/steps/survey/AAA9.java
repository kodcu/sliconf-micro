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
public class AAA9 extends SpringBootTestConfig { // NOSONAR

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
        assertTrue(initialData.checkSurveyCreateErrorMessages(exceptedErrorMessage, initialData.survey));
    }
}
