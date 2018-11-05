package javaday.istanbul.sliconf.micro.steps.survey;

import cucumber.api.PendingException;
import cucumber.api.java.tr.*;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.controller.event.GetEventWithKeyRoute;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.survey.GeneralException;
import javaday.istanbul.sliconf.micro.survey.model.Survey;
import javaday.istanbul.sliconf.micro.survey.service.GeneralService;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

@Ignore
public class GetSurveyTest extends SpringBootTestConfig { // NOSONAR

    @Autowired
    InitialData initialData;

    @Autowired
    GeneralService generalService;

    @Autowired
    private GetEventWithKeyRoute getEventWithKeyRoute;

    @Diyelimki("^Kullanıcı bir etkinlikteki bir anketin bilgilerine erişmek istiyor$")
    public void kullanıcıBirEtkinliktekiBirAnketinBilgilerineErişmekİstiyor() throws Throwable {
        initialData.init();
        initialData.surveyService.addNewSurvey(initialData.survey, initialData.event.getKey());
        try {
            generalService.findUserById(initialData.user.getId());

        } catch (GeneralException exp) {
            fail();
        }
    }


    @Eğerki("^Kullanıcı sistemde mevcut olan bir etkinlikteki bir anketin bilgilerine erişmek istiyor ise$")
    public void kullanıcıSistemdeMevcutOlanBirEtkinliktekiBirAnketinBilgilerineErişmekIstiyorIse() throws Throwable {

        ResponseMessage responseMessage;

        responseMessage = getEventWithKeyRoute.
                getEventWithKey(initialData.survey.getEventKey(), initialData.user.getId(), "false");
        assertTrue(responseMessage.isStatus());

    }

    @Ve("^Anket silinmemiş ise$")
    public void anketSilinmemişIse() throws Throwable {

        ResponseMessage responseMessage;
        int oldSurveyViewersCount = initialData.survey.getViewers();

        responseMessage = initialData.surveyService.getSurvey(initialData.survey.getId());
        assertTrue(responseMessage.isStatus());

        responseMessage = initialData.surveyService
                .updateSurveyViewers
                        (initialData.user.getId(), initialData.survey.getId(), initialData.event.getId());
        String exceptedMessage = initialData.env.getProperty("userHasAddedSurveyViewerList");
        assertEquals(exceptedMessage, responseMessage.getMessage());
        assertNotEquals(initialData.survey.getViewers().intValue(), oldSurveyViewersCount);

        // ayni kullanici bi daha anketi goruntulerse listeye eklenmemeli.
        oldSurveyViewersCount = initialData.survey.getViewers();
        responseMessage = initialData.surveyService
                .updateSurveyViewers
                        (initialData.user.getId(), initialData.survey.getId(), initialData.event.getId());
        exceptedMessage = initialData.env.getProperty("userAlreadyViewedSurvey");
        assertEquals(exceptedMessage, responseMessage.getMessage());
        assertEquals(oldSurveyViewersCount, initialData.survey.getViewers().intValue());
    }

    @Ozaman("^Sistem kullanıcıya istediği anketin bilgilerini gönderir$")
    public void sistemKullanıcıyaIstediğiAnketinBilgileriniGönderir() throws Throwable {

        Survey newSurvey = (Survey) initialData.surveyService.getSurvey(initialData.survey.getId()).getReturnObject();
        assertEquals(newSurvey.hashCode(), initialData.survey.hashCode());
    }

    @Eğerki("^Kullanıcı sistemde mevcut olmayan bir etkinlikteki bir anketin bilgilerine erişmek istiyor ise$")
    public void kullanıcıSistemdeMevcutOlmayanBirEtkinliktekiBirAnketinBilgilerineErişmekIstiyorIse() throws RuntimeException {

        Survey newSurvey = Survey.builder().build();
        newSurvey.setId(initialData.survey.getId());
        try {
            generalService.findEventByIdOrEventKey(newSurvey.getId());
        } catch (GeneralException e) {
            assertEquals(e.getClass(), GeneralException.class);
        }

    }

    @Ozaman("^Sistem kullanıcıya böyle bir etkinliğin sistemde mevcut olmadığına dair bir uyarı mesajı gönderir$")
    public void sistemKullanıcıyaBöyleBirEtkinliğinSistemdeMevcutOlmadığınaDairBirUyarıMesajıGönderir() throws Throwable {

        Survey newSurvey = Survey.builder().build();
        newSurvey.setId(initialData.survey.getId());
        String exceptedErrorMessage = initialData.env.getProperty("eventCanNotFoundWithGivenId");
        try {
            initialData.surveyService.getSurvey(newSurvey.getId());
        } catch (GeneralException e) {
            assertEquals(exceptedErrorMessage, e.getMessage());
        }
    }

    @Fakat("^Kullanıcı sistemde mevcut olmayan bir anketin bilgilerine erişmek istiyor ise$")
    public void kullanıcıSistemdeMevcutOlmayanBirAnketinBilgilerineErişmekIstiyorIse() throws Throwable {

        Survey newSurvey = Survey.builder().build();
        newSurvey.setId("!@#%^&");
        newSurvey.setEventId(initialData.survey.getEventId());
        try {
            initialData.surveyService.getSurvey(newSurvey.getId());
        } catch (GeneralException e) {
            assertEquals(e.getClass(), GeneralException.class);
        }
    }

    @Ozaman("^Sistem kullanıcıya böyle bir anketin sistemde mevcut olmadığına dair bir uyarı mesajı gönderir$")
    public void sistemKullanıcıyaBöyleBirAnketinSistemdeMevcutOlmadığınaDairBirUyarıMesajıGönderir() throws Throwable {

        Survey newSurvey = Survey.builder().build();
        newSurvey.setEventId(initialData.survey.getEventId());
        newSurvey.setId("!@#$^");
        String exceptedErrorMessage = initialData.env.getProperty("surveyCanNotFoundWithGivenId");
        try {
            initialData.surveyService.getSurvey(newSurvey.getId());
        } catch (GeneralException e) {
            assertEquals(exceptedErrorMessage, e.getMessage());
        }
    }
}
