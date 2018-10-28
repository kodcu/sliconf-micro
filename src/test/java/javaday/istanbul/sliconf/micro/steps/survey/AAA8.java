package javaday.istanbul.sliconf.micro.steps.survey;

import cucumber.api.java.tr.Fakat;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@Ignore
public class AAA8 extends SpringBootTestConfig { // NOSONAR

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
        assertTrue(initialData.checkSurveyCreateErrorMessages(exceptedErrorMessage, initialData.survey));
    }
}
