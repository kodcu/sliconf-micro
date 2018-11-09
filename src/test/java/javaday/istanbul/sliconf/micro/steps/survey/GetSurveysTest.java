package javaday.istanbul.sliconf.micro.steps.survey;

import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.event.controller.GetEventWithKeyRoute;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.survey.SurveyRepository;
import javaday.istanbul.sliconf.micro.survey.model.Survey;
import javaday.istanbul.sliconf.micro.survey.util.SurveyGenerator;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

@Ignore
public class GetSurveysTest extends SpringBootTestConfig { // NOSONAR

    @Autowired
    InitialData initialData;

    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    GetEventWithKeyRoute getEventWithKeyRoute;

    @Diyelimki("^Etkinlik yöneticisi etkinliğindeki tüm anketlerin bilgilerine erişmek istiyor$")
    public void etkinlikYöneticisiEtkinliğindekiTümAnketlerinBilgilerineErişmekIstiyor() throws Throwable {
        initialData.init();
        surveyRepository.deleteAll();
        surveyRepository
                .save(SurveyGenerator.generateRandomSurveys(25, initialData.event, initialData.user.getId()));

    }

    @Eğerki("^Etkinlik yöneticisi sistemde mevcut olan etkinliğinin tüm anketlerinin bilgilerine erişmek istiyor ise$")
    public void etkinlikYöneticisiSistemdeMevcutOlanEtkinliğininTümAnketlerininBilgilerineErişmekIstiyorIse() throws Throwable {
        ResponseMessage responseMessage;

        responseMessage = getEventWithKeyRoute.
                getEventWithKey(initialData.survey.getEventKey(), initialData.user.getId(), "false");
        assertTrue(responseMessage.isStatus());

    }

    @Ozaman("^Sistem etkinlik yöneticisine etkinlikte var olan tüm anketlerin bilgilerini gönderir$")
    public void sistemEtkinlikYöneticisineEtkinlikteVarOlanTümAnketlerinBilgileriniGönderir() throws Throwable {

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
