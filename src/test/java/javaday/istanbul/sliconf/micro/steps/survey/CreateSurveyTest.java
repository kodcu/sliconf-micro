package javaday.istanbul.sliconf.micro.steps.survey;

import com.couchbase.client.java.document.json.JsonObject;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import cucumber.api.java.tr.Ve;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.builder.EventBuilder;
import javaday.istanbul.sliconf.micro.builder.UserBuilder;
import javaday.istanbul.sliconf.micro.controller.event.CreateEventRoute;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.UserPassService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.survey.model.Question;
import javaday.istanbul.sliconf.micro.survey.model.QuestionOption;
import javaday.istanbul.sliconf.micro.survey.model.Survey;
import lombok.AllArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {CucumberConfiguration.class})
@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class CreateSurveyTest {// NOSONAR

    @Autowired
    private  UserRepositoryService userRepositoryService;
    @Autowired
    private  CreateEventRoute createEventRoute;

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
                .setDate(LocalDateTime.now().plusMonths(1)).build();
//        ResponseMessage createEventMessage1 = createEventRoute.processEvent(event, userId1);
//        assertTrue(createEventMessage1.isStatus());
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
        List<Question> questions = new ArrayList<>();
        questions.add(question);

        QuestionOption questionOption1 = new QuestionOption();
        QuestionOption questionOption2 = new QuestionOption();
        questionOption1.setText("Seçenek 01");
        questionOption2.setText("Seçenek 02");
        List<QuestionOption> questionOptions = new ArrayList<>();
        questionOptions.add(questionOption1);
        questionOptions.add(questionOption2);
        question.setOptions(questionOptions);

        survey.setQuestions(questions);

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
        boolean a = LocalDateTime.now().isBefore(LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.UTC));
        assertTrue(a);
    }

    @Ve("^Anketin bitiş tarihi başlangıç tarihinden önce değil ise$")
    public void anketinBitişTarihiBaşlangıçTarihindenÖnceDeğilIse() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Ve("^Anket en az bir soru içeriyorsa$")
    public void anketEnAzBirSoruIçeriyorsa() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Ve("^Anketteki soruların isimleri geçerli ise$")
    public void ankettekiSorularınIsimleriGeçerliIse() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Ve("^Anketteki sorular en az iki şık içeriyorsa$")
    public void ankettekiSorularEnAzIkiŞıkIçeriyorsa() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Ve("^Anketteki soru şıklarının isimleri geçerli ise$")
    public void ankettekiSoruŞıklarınınIsimleriGeçerliIse() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Ozaman("^Sistem anketi kayıt eder ve etkinlik sahibi anketi oluşturmuş olur$")
    public void sistemAnketiKayıtEderVeEtkinlikSahibiAnketiOluşturmuşOlur() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
