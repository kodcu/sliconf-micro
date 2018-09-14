package javaday.istanbul.sliconf.micro.steps.survey;

import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import cucumber.api.java.tr.Ve;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.controller.event.CreateEventRoute;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.survey.service.SurveyService;
import javaday.istanbul.sliconf.micro.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsEmptyCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;

@Slf4j
@ContextConfiguration(classes = {CucumberConfiguration.class})
@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class AAA1 {// NOSONAR

    @Autowired
    private InitialData initialData;

    @Autowired
    protected UserRepositoryService userRepositoryService;
    @Autowired
    protected CreateEventRoute createEventRoute;

    @Autowired
    protected EventRepositoryService eventRepositoryService;

    @Autowired
    protected SurveyService surveyService;



    @Diyelimki("^Etkinlik sahibi kendi etkinliğine bir anket eklemek istiyor$")
    public void etkinlikSahibiKendiEtkinliğineBirAnketEklemekIstiyor() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        initialData.init();
    }

    @Eğerki("^Etkinlik sahibi geçerli bir anket ismi vermiş ise$")
    public void etkinlikSahibiGeçerliBirAnketIsmiVermişIse() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        assertThat(initialData.survey.getName(), not(isEmptyOrNullString()));

        assertThat(initialData.survey.getName().length(), greaterThanOrEqualTo(Constants.EVENT_MIN_NAME_LENGTH));
        assertThat(initialData.survey.getName().length(), lessThanOrEqualTo(Constants.EVENT_MAX_NAME_LENGTH));

        Pattern pattern = Pattern.compile("^[\\w\\s]+[\\w\\d\\s]+$", Pattern.UNICODE_CHARACTER_CLASS);
        assertTrue(pattern.matcher(initialData.survey.getName()).matches());

    }

    @Ve("^Anketin başlangıç ve bitiş tarihi geçerli formatta ise$")
    public void anketinBaşlangıçVeBitişTarihiGeçerliFormattaIse() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        assertTrue(initialData.survey.getStartTime().matches("^\\d+\\d$"));
        assertTrue(initialData.survey.getEndTime().matches("^\\d+\\d$"));
    }

    @Ve("^Anketin başlangıç tarihi bugün veya daha ileri bir tarih olarak belirtilmiş ise$")
    public void anketinBaşlangıçTarihiBugünVeyaDahaIleriBirTarihOlarakBelirtilmişIse() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        long epochSecond = Long.parseLong(initialData.survey.getStartTime());
        LocalDateTime startTime = LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.UTC);
        assertTrue(LocalDateTime.now(ZoneOffset.UTC).isBefore(startTime));
    }

    @Ve("^Anketin bitiş tarihi başlangıç tarihinden önce değil ise$")
    public void anketinBitişTarihiBaşlangıçTarihindenÖnceDeğilIse() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        long epochSecondStartTime = Long.parseLong(initialData.survey.getStartTime());
        long epochSecondEndTime = Long.parseLong(initialData.survey.getEndTime());
        LocalDateTime startTime = LocalDateTime.ofEpochSecond(epochSecondStartTime, 0, ZoneOffset.UTC);
        LocalDateTime endtime = LocalDateTime.ofEpochSecond(epochSecondEndTime, 0, ZoneOffset.UTC);
        assertTrue(startTime.isBefore(endtime));
    }

    @Ve("^Anket en az bir soru içeriyorsa$")
    public void anketEnAzBirSoruIçeriyorsa() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        assertThat(initialData.survey.getQuestions(), Matchers.not(IsEmptyCollection.empty()));
    }

    @Ve("^Anketteki soruların isimleri geçerli ise$")
    public void ankettekiSorularınIsimleriGeçerliIse() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        initialData.survey.getQuestions().forEach(question -> assertThat(question.getText(), Matchers.not(isEmptyString())));
    }

    @Ve("^Anketteki sorular en az iki şık içeriyorsa$")
    public void ankettekiSorularEnAzIkiŞıkIçeriyorsa() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        initialData.survey.getQuestions().forEach(question -> assertThat(question.getOptions(), hasSize(2)));
    }

    @Ve("^Anketteki soru şıklarının isimleri geçerli ise$")
    public void ankettekiSoruŞıklarınınIsimleriGeçerliIse() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        initialData.survey.getQuestions()
                .forEach(question -> question.getOptions()
                        .forEach(questionOption ->
                                assertThat(questionOption.getText(), Matchers.not(isEmptyString()))));
    }

    @Ozaman("^Sistem anketi kayıt eder ve etkinlik sahibi anketi oluşturmuş olur$")
    public void sistemAnketiKayıtEderVeEtkinlikSahibiAnketiOluşturmuşOlur() throws Throwable {
        ResponseMessage responseMessage = new ResponseMessage();

        responseMessage = surveyService.addNewSurvey(initialData.survey, eventKey);
        log.info(responseMessage.getMessage());

        assertTrue(responseMessage.isStatus());

    }


}
