package javaday.istanbul.sliconf.micro.steps.survey;

import cucumber.api.PendingException;
import cucumber.api.java.tr.Fakat;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.survey.service.SurveyService;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsEmptyCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@Slf4j
@ContextConfiguration(classes = {CucumberConfiguration.class})
@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class AAA6 {

    @Autowired
    SurveyService surveyService;
    @Autowired
    private InitialData initialData;
    
    @Fakat("^Anket en az bir soru içermiyor ise$")
    public void anketEnAzBirSoruIçermiyorIse() throws Throwable {
        initialData.survey.setQuestions(new ArrayList<>());
        assertThat(initialData.survey.getQuestions(), (IsEmptyCollection.empty()));

    }

    @Ozaman("^Sistem anketi kayıt etmez ve ön tarafa ankette en az bir soru olmalı gibi bir hata mesajı gönderilir$")
    public void sistemAnketiKayıtEtmezVeÖnTarafaAnketteEnAzBirSoruOlmalıGibiBirHataMesajıGönderilir() throws Throwable {
        String exceptedErrorMessage = "Survey questions can not be empty! ";
        assertTrue(initialData.checkErrorMessages(exceptedErrorMessage, initialData.survey));
    }
}
