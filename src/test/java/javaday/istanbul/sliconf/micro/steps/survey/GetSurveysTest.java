package javaday.istanbul.sliconf.micro.steps.survey;

import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.controller.event.GetEventWithKeyRoute;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.survey.SurveyRepository;
import javaday.istanbul.sliconf.micro.survey.model.Survey;
import javaday.istanbul.sliconf.micro.survey.util.SurveyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.Assert.*;

@Slf4j
@ContextConfiguration(classes = {CucumberConfiguration.class})
@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class GetSurveysTest { // NOSONAR

    @Autowired
    InitialData initialData;

    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    GetEventWithKeyRoute getEventWithKeyRoute;

    @Diyelimki("^Kullanıcı bir etkinlikteki tüm anketlerin bilgilerine erişmek istiyor$")
    public void kullanıcıBirEtkinliktekiTümAnketlerinBilgilerineErişmekIstiyor() throws Throwable {
        initialData.init();
        surveyRepository.deleteAll();
        surveyRepository
                .save(SurveyGenerator.generateRandomSurveys(25, initialData.event, initialData.user.getId()));

    }

    @Eğerki("^Kullanıcı sistemde mevcut olan bir etkinlikteki tüm anketlerin bilgilerine erişmek istiyor ise$")
    public void kullanıcıSistemdeMevcutOlanBirEtkinliktekiTümAnketlerinBilgilerineErişmekIstiyorIse() throws Throwable {
        ResponseMessage responseMessage;

        responseMessage = getEventWithKeyRoute.
                getEventWithKey(initialData.survey.getEventKey(), initialData.user.getId(), "false");
        assertTrue(responseMessage.isStatus());

    }

    @Ozaman("^Sistem kullanıcıya etkinlikte var olan tüm anketlerin bilgilerini gönderir$")
    public void sistemKullanıcıyaEtkinlikteVarOlanTümAnketlerinBilgileriniGönderir() throws Throwable {

        ResponseMessage responseMessage;
        responseMessage = initialData.surveyService.getSurveys(initialData.event.getId());
        String exceptedReturnMessage = initialData.env.getProperty("surveysListedSuccessfully");
        assertEquals(exceptedReturnMessage, responseMessage.getMessage());
        assertTrue(responseMessage.isStatus());

        List<Survey> surveys = (List<Survey>) responseMessage.getReturnObject();
        assertThat(surveys, Matchers.hasSize(25));

        surveyRepository.deleteAll();
        responseMessage = initialData.surveyService.getSurveys(initialData.event.getId());
        exceptedReturnMessage = initialData.env.getProperty("surveysListedSuccessfully");
        assertEquals(exceptedReturnMessage, responseMessage.getMessage());
        assertTrue(responseMessage.isStatus());
        responseMessage = initialData.surveyService.getSurveys(initialData.event.getId());
        surveys = (List<Survey>) responseMessage.getReturnObject();
        assertThat(surveys, Matchers.hasSize(0));
    }


}
