package javaday.istanbul.sliconf.micro.steps.survey;

import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import cucumber.api.java.tr.Ve;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.survey.GeneralException;
import javaday.istanbul.sliconf.micro.survey.SurveyMessageProvider;
import javaday.istanbul.sliconf.micro.survey.SurveyRepository;
import javaday.istanbul.sliconf.micro.survey.model.Survey;
import javaday.istanbul.sliconf.micro.survey.util.SurveyGenerator;
import javaday.istanbul.sliconf.micro.survey.util.SurveyUtil;
import javaday.istanbul.sliconf.micro.util.Constants;
import javaday.istanbul.sliconf.micro.util.json.JsonUtil;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;

@Ignore
public class AAA1 extends SpringBootTestConfig { // NOSONAR

    @Autowired
    InitialData initialData;

    @Autowired
    SurveyMessageProvider surveyMessageProvider;

    @Autowired
    SurveyRepository surveyRepository;

    @Diyelimki("^Etkinlik sahibi kendi etkinliğine bir anket eklemek istiyor$")
    public void etkinlikSahibiKendiEtkinliğineBirAnketEklemekIstiyor() throws Throwable {
        initialData.init();
        String jsonTestBody = "{\"q!@!#!#$!@$\":\"!@#$%*()\"}";
        try {
            JsonUtil.fromJsonOrElseThrow(jsonTestBody, Survey.class);
        } catch (RuntimeException e) {
            assertEquals(e.getClass().getName(), GeneralException.class.getName());
        }
    }

    @Eğerki("^Etkinlik sahibi geçerli bir anket ismi vermiş ise$")
    public void etkinlikSahibiGeçerliBirAnketIsmiVermişIse() throws Throwable {

        assertThat(initialData.survey.getName(), not(isEmptyOrNullString()));

        assertThat(initialData.survey.getName().length(), greaterThanOrEqualTo(Constants.EVENT_MIN_NAME_LENGTH));
        assertThat(initialData.survey.getName().length(), lessThanOrEqualTo(Constants.EVENT_MAX_NAME_LENGTH));

        Pattern pattern = Pattern.compile("^[\\w\\s]+[\\w\\d\\s]+$", Pattern.UNICODE_CHARACTER_CLASS);
        assertTrue(pattern.matcher(initialData.survey.getName()).matches());

    }

    @Ve("^Anketin başlangıç ve bitiş tarihi geçerli formatta ise$")
    public void anketinBaşlangıçVeBitişTarihiGeçerliFormattaIse() throws Throwable {

        assertTrue(initialData.survey.getStartTime().matches("^\\d+\\d$"));
        assertTrue(initialData.survey.getEndTime().matches("^\\d+\\d$"));
    }

    @Ve("^Anketin başlangıç tarihi bugün veya daha ileri bir tarih olarak belirtilmiş ise$")
    public void anketinBaşlangıçTarihiBugünVeyaDahaIleriBirTarihOlarakBelirtilmişIse() throws Throwable {

        initialData.survey.setStartTime(null);
        initialData.survey.setEndTime(null);

        SurveyUtil.generateDates(initialData.survey, initialData.event);
        assertNotNull(initialData.survey.getStartTime());
        assertNotNull(initialData.survey.getEndTime());

        long epochSecond = Long.parseLong(initialData.survey.getStartTime());
        LocalDateTime startTime = LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.UTC);
        assertTrue(LocalDateTime.now(ZoneOffset.UTC).isBefore(startTime));
    }

    @Ve("^Anketin bitiş tarihi başlangıç tarihinden önce değil ise$")
    public void anketinBitişTarihiBaşlangıçTarihindenÖnceDeğilIse() throws Throwable {

        long epochSecondStartTime = Long.parseLong(initialData.survey.getStartTime());
        long epochSecondEndTime = Long.parseLong(initialData.survey.getEndTime());
        LocalDateTime startTime = LocalDateTime.ofEpochSecond(epochSecondStartTime, 0, ZoneOffset.UTC);
        LocalDateTime endTime = LocalDateTime.ofEpochSecond(epochSecondEndTime, 0, ZoneOffset.UTC);
        assertTrue(startTime.isBefore(endTime));
    }

    @Ve("^Anket en az bir soru içeriyorsa$")
    public void anketEnAzBirSoruIçeriyorsa() throws Throwable {

        assertThat(initialData.survey.getQuestions(), Matchers.not(IsEmptyCollection.empty()));
    }

    @Ve("^Anketteki soruların isimleri geçerli ise$")
    public void ankettekiSorularınIsimleriGeçerliIse() throws Throwable {

        initialData.survey.getQuestions().forEach(question -> assertThat(question.getText(), Matchers.not(isEmptyString())));
    }

    @Ve("^Anketteki sorular en az iki şık içeriyorsa$")
    public void ankettekiSorularEnAzIkiŞıkIçeriyorsa() throws Throwable {

        initialData.survey.getQuestions().forEach(question -> assertThat(question.getOptions(), hasSize(2)));
    }

    @Ve("^Anketteki soru şıklarının isimleri geçerli ise$")
    public void ankettekiSoruŞıklarınınIsimleriGeçerliIse() throws Throwable {

        initialData.survey.getQuestions()
                .forEach(question -> question.getOptions()
                        .forEach(questionOption ->
                                assertThat(questionOption.getText(), Matchers.not(isEmptyString()))));
    }

    @Ozaman("^Sistem anketi kayıt eder ve etkinlik sahibi anketi oluşturmuş olur$")
    public void sistemAnketiKayıtEderVeEtkinlikSahibiAnketiOluşturmuşOlur() throws Throwable {

        ResponseMessage responseMessage;
        Survey oldSurvey = SurveyGenerator.buildFromOther(initialData.survey);

        oldSurvey.getQuestions().forEach(question -> question.setId(null));
        oldSurvey.getQuestions()
                .forEach(question -> question
                        .getOptions().forEach(questionOption -> questionOption.setId(null)));

        responseMessage = initialData.surveyService.addNewSurvey(initialData.survey, initialData.event.getKey());

        assertTrue(responseMessage.isStatus());
        assertEquals(initialData.env.getProperty("surveyCreatedSuccessfully"), responseMessage.getMessage());

        Survey newSurvey = (Survey) responseMessage.getReturnObject();

        assertNotNull(newSurvey.getId());

        assertNotEquals(oldSurvey.hashCode(), newSurvey.hashCode());
        assertNotEquals(oldSurvey, newSurvey);

        newSurvey.getQuestions().forEach(question -> assertThat(question.getId(), not(isEmptyOrNullString())));
        newSurvey.getQuestions()
                .forEach(question -> question
                        .getOptions()
                        .forEach(questionOption -> assertThat(questionOption.getId(), not(isEmptyOrNullString()))));
    }
}
