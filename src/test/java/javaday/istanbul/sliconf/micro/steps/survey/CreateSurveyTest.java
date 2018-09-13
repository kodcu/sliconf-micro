package javaday.istanbul.sliconf.micro.steps.survey;

import com.couchbase.client.java.document.json.JsonObject;
import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import cucumber.api.java.tr.Ve;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.builder.EventBuilder;
import javaday.istanbul.sliconf.micro.controller.event.CreateEventRoute;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.survey.model.Question;
import javaday.istanbul.sliconf.micro.survey.model.QuestionOption;
import javaday.istanbul.sliconf.micro.survey.model.Survey;
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

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;

@Slf4j
@ContextConfiguration(classes = {CucumberConfiguration.class})
@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class CreateSurveyTest {// NOSONAR

    @Autowired
    private UserRepositoryService userRepositoryService;
    @Autowired
    private CreateEventRoute createEventRoute;

    @Autowired
    private EventRepositoryService eventRepositoryService;

    @Autowired
    private SurveyService surveyService;

    private JsonObject request = JsonObject.create();

    private User user;

    private Event event;

    private Survey survey;

    void init() {
        user = new User();
        user.setUsername("createSurveyUser01");
        user.setEmail("createSurveyUser01@sliconf.com");
        user.setPassword("123123123");

        ResponseMessage savedUserMessage = userRepositoryService.saveUser(user);

        String userId1 = ((User) savedUserMessage.getReturnObject()).getId();

        event = new EventBuilder()
                .setName("Create Event 01")
                .setExecutiveUser(userId1)
                .setDate(LocalDateTime.now().plusMonths(1))

                .build();
//        ResponseMessage createEventMessage1 = createEventRoute.processEvent(event, userId1);
//        assertTrue(createEventMessage1.isStatus());
        eventRepositoryService.save(event);
        event.setId(eventRepositoryService.findByName(event.getName()).get(0).getId());
    }

    @Diyelimki("^Etkinlik sahibi kendi etkinliğine bir anket eklemek istiyor$")
    public void etkinlikSahibiKendiEtkinliğineBirAnketEklemekIstiyor() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        this.init();
        survey = new Survey();
        survey.setEventId(event.getId());
        survey.setUserId(user.getId());
        survey.setName("Anket 01");

        long date = LocalDateTime.now().plusDays(1).toEpochSecond(ZoneOffset.UTC);
        survey.setStartTime(Long.toString(date));
        long date2 = LocalDateTime.now().plusDays(2).toEpochSecond(ZoneOffset.UTC);
        survey.setEndTime(Long.toString(date2));

        Question question = new Question();
        question.setText("Soru 01");
        question.setId("questionId");
        List<Question> questions = new ArrayList<>();
        questions.add(question);

        QuestionOption questionOption1 = new QuestionOption();
        QuestionOption questionOption2 = new QuestionOption();
        questionOption1.setText("Seçenek 01"); questionOption1.setId("questionOption1Id");
        questionOption2.setText("Seçenek 02"); questionOption2.setId("questionOption2Id");
        List<QuestionOption> questionOptions = new ArrayList<>();
        questionOptions.add(questionOption1);
        questionOptions.add(questionOption2);
        question.setOptions(questionOptions);

        survey.setQuestions(questions);
        survey.setEventId(event.getId());

    }

    @Eğerki("^Etkinlik sahibi geçerli bir anket ismi vermiş ise$")
    public void etkinlikSahibiGeçerliBirAnketIsmiVermişIse() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        assertNotNull(event.getName());
        assertNotEquals("", event.getName());

    }

    @Ve("^Anketin başlangıç tarihi bugün veya daha ileri bir tarih olarak belirtilmiş ise$")
    public void anketinBaşlangıçTarihiBugünVeyaDahaIleriBirTarihOlarakBelirtilmişIse() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        long epochSecond = Long.parseLong(survey.getStartTime());
        LocalDateTime startTime = LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.UTC);
        assertTrue(LocalDateTime.now(ZoneOffset.UTC).isBefore(startTime));
    }

    @Ve("^Anketin bitiş tarihi başlangıç tarihinden önce değil ise$")
    public void anketinBitişTarihiBaşlangıçTarihindenÖnceDeğilIse() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        long epochSecondStartTime = Long.parseLong(survey.getStartTime());
        long epochSecondEndTime = Long.parseLong(survey.getEndTime());
        LocalDateTime startTime = LocalDateTime.ofEpochSecond(epochSecondStartTime, 0, ZoneOffset.UTC);
        LocalDateTime endtime = LocalDateTime.ofEpochSecond(epochSecondEndTime, 0, ZoneOffset.UTC);
        assertTrue(startTime.isBefore(endtime));
    }

    @Ve("^Anket en az bir soru içeriyorsa$")
    public void anketEnAzBirSoruIçeriyorsa() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        assertThat(survey.getQuestions(), Matchers.not(IsEmptyCollection.empty()));
    }

    @Ve("^Anketteki soruların isimleri geçerli ise$")
    public void ankettekiSorularınIsimleriGeçerliIse() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        survey.getQuestions().forEach(question -> assertThat(question.getText(), Matchers.not(isEmptyString())));
    }

    @Ve("^Anketteki sorular en az iki şık içeriyorsa$")
    public void ankettekiSorularEnAzIkiŞıkIçeriyorsa() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        survey.getQuestions().forEach(question -> assertThat(question.getOptions(), hasSize(2)));
    }

    @Ve("^Anketteki soru şıklarının isimleri geçerli ise$")
    public void ankettekiSoruŞıklarınınIsimleriGeçerliIse() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        survey.getQuestions()
                .forEach(question -> question.getOptions()
                        .forEach(questionOption ->
                                assertThat(questionOption.getText(), Matchers.not(isEmptyString()))));
    }

    @Ozaman("^Sistem anketi kayıt eder ve etkinlik sahibi anketi oluşturmuş olur$")
    public void sistemAnketiKayıtEderVeEtkinlikSahibiAnketiOluşturmuşOlur() throws Throwable {
        ResponseMessage responseMessage = new ResponseMessage();

        responseMessage = surveyService.addNewSurvey(survey);
        log.info(responseMessage.getMessage());

        assertTrue(responseMessage.isStatus());

    }
}
