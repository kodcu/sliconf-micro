package javaday.istanbul.sliconf.micro.steps.survey;

import cucumber.api.java.tr.Fakat;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@Ignore
public class AAA6 extends SpringBootTestConfig { // NOSONAR

    @Autowired
    private InitialData initialData;

    @Fakat("^Anket en az bir soru içermiyor ise$")
    public void anketEnAzBirSoruIçermiyorIse() throws Throwable {
        initialData.survey.setQuestions(new ArrayList<>());
        assertThat(initialData.survey.getQuestions(), (IsEmptyCollection.empty()));

    }

    @Ozaman("^Sistem anketi kayıt etmez ve ön tarafa ankette en az bir soru olmalı gibi bir hata mesajı gönderilir$")
    public void sistemAnketiKayıtEtmezVeÖnTarafaAnketteEnAzBirSoruOlmalıGibiBirHataMesajıGönderilir() throws Throwable {
        String exceptedErrorMessage = initialData.env.getProperty("survey.questions.empty");
        assertTrue(initialData.checkSurveyCreateErrorMessages(exceptedErrorMessage, initialData.survey));
    }
}
