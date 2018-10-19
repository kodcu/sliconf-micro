package javaday.istanbul.sliconf.micro.steps.survey;

import com.devskiller.jfairy.Fairy;
import cucumber.api.java.tr.Diyelimki;
import cucumber.api.java.tr.Eğerki;
import cucumber.api.java.tr.Ozaman;
import cucumber.api.java.tr.Ve;
import javaday.istanbul.sliconf.micro.CucumberConfiguration;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.survey.model.Question;
import javaday.istanbul.sliconf.micro.survey.model.QuestionOption;
import javaday.istanbul.sliconf.micro.survey.model.Survey;
import javaday.istanbul.sliconf.micro.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
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
public class UpdateSurveyTest { // NOSONAR

    @Autowired
    InitialData initialData;

    private Survey survey;

    @Diyelimki("^Etkinlik sahibi sistemde mevcut, daha önceden oluşturmuş olduğu bir anketi güncellemek istiyor$")
    public void etkinlikSahibiSistemdeMevcutDahaÖncedenOluşturmuşOlduğuBirAnketiGüncellemekIstiyor() throws Throwable {

        initialData.init();
        initialData.surveyService.addNewSurvey(initialData.survey, initialData.event.getId());

        ResponseMessage responseMessage = initialData.surveyService.getSurvey(initialData.survey.getId());
        assertEquals(responseMessage.getReturnObject().hashCode(), initialData.survey.hashCode());
        survey = (Survey) responseMessage.getReturnObject();
    }

    @Eğerki("^Etkinlik sahibi güncellenen anket için geçerli bir anket ismi vermiş ise$")
    public void etkinlikSahibiGüncellenenAnketIçinGeçerliBirAnketIsmiVermişIse() throws Throwable {
        survey.setName("Updated Survey Name");

        assertThat(survey.getName(), not(isEmptyOrNullString()));

        assertThat(survey.getName().length(), greaterThanOrEqualTo(Constants.EVENT_MIN_NAME_LENGTH));
        assertThat(survey.getName().length(), lessThanOrEqualTo(Constants.EVENT_MAX_NAME_LENGTH));

        Pattern pattern = Pattern.compile("^[\\w\\s]+[\\w\\d\\s]+$", Pattern.UNICODE_CHARACTER_CLASS);
        assertTrue(pattern.matcher(survey.getName()).matches());


    }

    @Ve("^Güncellenen anketin başlangıç ve bitiş tarihi geçerli formatta ise$")
    public void güncellenenAnketinBaşlangıçVeBitişTarihiGeçerliFormattaIse() throws Throwable {

        LocalDateTime sTime = LocalDateTime.ofEpochSecond(Long.valueOf(initialData.survey.getStartTime()), 0, ZoneOffset.UTC);
        LocalDateTime eTime = LocalDateTime.ofEpochSecond(Long.valueOf(initialData.survey.getEndTime()), 0, ZoneOffset.UTC);

        survey.setStartTime(String.valueOf(sTime.plusHours(3).toEpochSecond(ZoneOffset.UTC)));
        survey.setEndTime(String.valueOf(eTime.plusHours(3).toEpochSecond(ZoneOffset.UTC)));

        assertTrue(survey.getStartTime().matches("^\\d+\\d$"));
        assertTrue(survey.getEndTime().matches("^\\d+\\d$"));

    }

    @Ve("^Güncellenen anketin başlangıç tarihi bugün veya daha ileri bir tarih olarak belirtilmiş ise$")
    public void güncellenenAnketinBaşlangıçTarihiBugünVeyaDahaIleriBirTarihOlarakBelirtilmişIse() throws Throwable {

        long epochSecond = Long.parseLong(survey.getStartTime());
        LocalDateTime startTime = LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.UTC);
        assertTrue(LocalDateTime.now(ZoneOffset.UTC).isBefore(startTime));
    }

    @Ve("^Güncellenen anketin bitiş tarihi başlangıç tarihinden önce değil ise$")
    public void güncellenenAnketinBitişTarihiBaşlangıçTarihindenÖnceDeğilIse() throws Throwable {

        long epochSecondStartTime = Long.parseLong(survey.getStartTime());
        long epochSecondEndTime = Long.parseLong(survey.getEndTime());
        LocalDateTime startTime = LocalDateTime.ofEpochSecond(epochSecondStartTime, 0, ZoneOffset.UTC);
        LocalDateTime endTime = LocalDateTime.ofEpochSecond(epochSecondEndTime, 0, ZoneOffset.UTC);
        assertTrue(startTime.isBefore(endTime));
    }

    @Ve("^Güncellenen anket en az bir soru içeriyorsa$")
    public void güncellenenAnketEnAzBirSoruIçeriyorsa() throws Throwable {
        // TODO: 23.10.2018 Gerekli util sınıflarını oluşturup verileri oradan üretmek daha doğru olur.
        Fairy fairy = Fairy.create();
        survey.setQuestions(new ArrayList<>());
        // generate questions
        for (int k = 0; k < fairy.baseProducer().randomBetween(1, 10); k++) {
            Question question = Question.builder()
                    .text(fairy.textProducer().latinSentence(20))
                    .id(new ObjectId().toString())
                    .options(new ArrayList<>())
                    .build();
            //generate question options.
            for (int j = 0; j < fairy.baseProducer().randomBetween(2, 5); j++) {
                QuestionOption questionOption = QuestionOption.builder()
                        .text(fairy.textProducer().latinSentence(20))
                        .id(new ObjectId().toString())
                        .build();
                question.getOptions().add(questionOption);
            }
            survey.getQuestions().add(question);
        }

        assertThat(survey.getQuestions(), Matchers.not(IsEmptyCollection.empty()));

    }

    @Ve("^Güncellenen anketteki soruların isimleri geçerli ise$")
    public void güncellenenAnkettekiSorularınIsimleriGeçerliIse() throws Throwable {

        survey.getQuestions().forEach(question -> assertThat(question.getText(), Matchers.not(isEmptyString())));


    }

    @Ve("^Güncellenen anketteki sorular en az iki şık içeriyorsa$")
    public void güncellenenAnkettekiSorularEnAzIkiŞıkIçeriyorsa() throws Throwable {

        survey.getQuestions().forEach(question -> assertThat(question.getOptions().size(), greaterThanOrEqualTo(2)));

    }

    @Ve("^Güncellenen anketteki soru şıklarının isimleri geçerli ise$")
    public void güncellenenAnkettekiSoruŞıklarınınIsimleriGeçerliIse() throws Throwable {

        survey.getQuestions()
              .forEach(question -> question.getOptions()
              .forEach(questionOption -> assertThat(questionOption.getText(), Matchers.not(isEmptyString()))));

    }

    @Ozaman("^Sistem anketin bilgilerini günceller ve güncelleme başarılı şeklinde bir mesaj üretir$")
    public void sistemAnketinBilgileriniGüncellerVeGüncellemeBaşarılıŞeklindeBirMesajÜretir() throws Throwable {

        ResponseMessage responseMessage;

        responseMessage = initialData.surveyService.updateSurvey(survey, initialData.event.getKey());

        assertEquals(survey.hashCode(), responseMessage.getReturnObject().hashCode());
        assertTrue(responseMessage.isStatus());
        assertEquals(initialData.env.getProperty("surveyUpdatedSuccessfully"), responseMessage.getMessage());


    }
}
