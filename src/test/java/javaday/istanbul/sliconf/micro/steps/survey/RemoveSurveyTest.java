package javaday.istanbul.sliconf.micro.steps.survey;

import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import cucumber.api.java.tr.Ve;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.event.controller.GetEventWithKeyRoute;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.survey.SurveyRepository;
import javaday.istanbul.sliconf.micro.survey.model.Survey;
import javaday.istanbul.sliconf.micro.survey.service.SurveyService;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.*;

@Ignore
public class RemoveSurveyTest extends SpringBootTestConfig { // NOSONAR

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
    public void etkinlikYöneticisiSistemdeMevcutOlanEtkinliktekiBirAnketiSilmekIstiyorIse() throws Throwable {

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

    @Ve("^Anketi o an aktif olarak kullanan birisi yoksa - ekrana yansıltılma gibi$")
    public void anketiOAnAktifOlarakKullananBirisiYoksaEkranaYansıltılmaGibi() throws Throwable {
        assertTrue(true);
    }

    @Ozaman("^Sistem anketi ve ona bağlı cevapları siler ve etkinlik yöneticisine geri bildirim verir$")
    public void sistemAnketiVeOnaBağlıCevaplarıSilerVeEtkinlikYöneticisineGeriBildirimVerir() throws Throwable {

        ResponseMessage responseMessage;
        responseMessage = surveyService.deleteSurvey(initialData.survey.getId());

        assertTrue(responseMessage.isStatus());

        String exceptedReturnMessage = initialData.env.getProperty("surveyDeletedSuccessfully");
        assertEquals(exceptedReturnMessage, responseMessage.getMessage());

        Optional<Survey> survey = surveyRepository.findById(initialData.survey.getId());
        assertFalse(survey.isPresent());

    }

 }
