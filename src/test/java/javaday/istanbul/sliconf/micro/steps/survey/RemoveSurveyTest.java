package javaday.istanbul.sliconf.micro.steps.survey;

import cucumber.api.PendingException;
import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import cucumber.api.java.tr.Ve;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.controller.event.GetEventWithKeyRoute;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.survey.SurveyRepository;
import javaday.istanbul.sliconf.micro.survey.model.Survey;
import javaday.istanbul.sliconf.micro.survey.service.SurveyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

import static org.junit.Assert.*;

@Slf4j
@ContextConfiguration(classes = {CucumberConfiguration.class})
@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class RemoveSurveyTest { // NOSONAR

    @Autowired
    InitialData initialData;

    @Autowired
    GetEventWithKeyRoute getEventWithKeyRoute;

    @Autowired
    SurveyService surveyService;

    @Autowired
    SurveyRepository surveyRepository;

    @Diyelimki("^Etkinlik yöneticisi kendi etkinliğindeki bir anketi silmek istiyor$")
    public void etkinlikYöneticisiKendiEtkinliğindekiBirAnketiSilmekIstiyor() throws Throwable {
        initialData.init();
        surveyRepository.save(initialData.survey);
    }

    @Eğerki("^Etkinlik yöneticisi sistemde mevcut olan etkinlikteki bir anketi silmek istiyor ise$")
    public  void etkinlikYöneticisiSistemdeMevcutOlanEtkinliktekiBirAnketiSilmekIstiyorIse() throws Throwable {

        ResponseMessage responseMessage;

        responseMessage = getEventWithKeyRoute.
                getEventWithKey(initialData.survey.getEventKey(), initialData.user.getId(), "false");
        assertTrue(responseMessage.isStatus());

    }

    @Ve("^Etkinlik yöneticisi sistemde mevcut olan bir anketi silmek istiyor ise$")
    public void etkinlikYöneticisiSistemdeMevcutOlanBirAnketiSilmekIstiyorIse() throws Throwable {

        ResponseMessage responseMessage;

        responseMessage = initialData.surveyService.getSurvey(initialData.survey.getId());
        assertTrue(responseMessage.isStatus());
        assertEquals(initialData.survey.hashCode(), responseMessage.getReturnObject().hashCode());

    }

    @Ozaman("^Sistem anketi ve var ise ankete verilmiş cevapları siler$")
    public void sistemAnketiVeVarIseAnketeVerilmişCevaplarıSiler() throws Throwable {

        ResponseMessage responseMessage;
        responseMessage = surveyService.deleteSurvey(initialData.survey.getId());

        assertTrue(responseMessage.isStatus());

        String exceptedReturnMessage = initialData.env.getProperty("surveyDeletedSuccessfully");
        assertEquals(exceptedReturnMessage, responseMessage.getMessage());

        Optional<Survey> survey = surveyRepository.findById(initialData.survey.getId());
        assertFalse(survey.isPresent());

    }
}
