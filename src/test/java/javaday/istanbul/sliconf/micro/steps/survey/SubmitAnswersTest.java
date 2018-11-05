package javaday.istanbul.sliconf.micro.steps.survey;

import cucumber.api.PendingException;
import cucumber.api.java.tr.*;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.controller.event.GetEventWithKeyRoute;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.survey.AnswerRepository;
import javaday.istanbul.sliconf.micro.survey.GeneralException;
import javaday.istanbul.sliconf.micro.survey.SurveyMessageProvider;
import javaday.istanbul.sliconf.micro.survey.SurveyRepository;
import javaday.istanbul.sliconf.micro.survey.model.Answer;
import javaday.istanbul.sliconf.micro.survey.model.Question;
import javaday.istanbul.sliconf.micro.survey.model.QuestionOption;
import javaday.istanbul.sliconf.micro.survey.model.Survey;
import javaday.istanbul.sliconf.micro.survey.service.AnswerService;
import javaday.istanbul.sliconf.micro.survey.util.SurveyGenerator;
import javaday.istanbul.sliconf.micro.survey.util.SurveyUtil;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@Ignore
public class SubmitAnswersTest extends SpringBootTestConfig { // NOSONAR

    @Autowired
    InitialData initialData;

    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    GetEventWithKeyRoute getEventWithKeyRoute;

    @Autowired
    AnswerService answerService;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    SurveyMessageProvider surveyMessageProvider;

    private Answer answer;

    private Survey answeredSurvey;

    private void setup() {

        answeredSurvey = SurveyGenerator.buildFromOther(initialData.survey);
        Survey equalSurvey = SurveyGenerator.buildFromOther(initialData.survey);

        assertEquals(equalSurvey.hashCode(), answeredSurvey.hashCode());
        assertEquals(equalSurvey, answeredSurvey);

        Survey notEqualSurvey = SurveyGenerator
                .generateRandomSurveys(1, initialData.event, initialData.user.getId()).get(0);
        assertNotEquals(notEqualSurvey.hashCode(), answeredSurvey.hashCode());
        assertNotEquals(notEqualSurvey, answeredSurvey);
    }

    @Diyelimki("^Kullanıcı etkinlikteki bir ankete cevap vermek istiyor$")
    public void kullanıcıEtkinliktekiBirAnketeCevapVermekIstiyor() throws Throwable {
        setup();
        initialData.init();
        initialData.survey = surveyRepository.save(initialData.survey);
        answer = Answer.builder()
                .eventId(initialData.event.getId())
                .eventKey(initialData.event.getKey())
                .surveyId(initialData.survey.getId())
                .userId(initialData.user.getId())
                .answeredQuestions(initialData.survey.getQuestions()
                        .stream()
                        .collect(Collectors.toMap(
                                Question::getId,
                                question -> question.getOptions()
                                        .stream()
                                        .findAny()
                                        .orElse(QuestionOption.builder().build()).getText())))
                .build();


    }

    @Eğerki("^Kullanıcı sistemde mevcut olan etkinlikteki bir ankete cevap vermek istiyor ise$")
    public void kullanıcıSistemdeMevcutOlanEtkinliktekiBirAnketeCevapVermekIstiyorIse() throws Throwable {
        ResponseMessage responseMessage;

        responseMessage = getEventWithKeyRoute.
                getEventWithKey(initialData.survey.getEventKey(), initialData.user.getId(), "false");
        assertTrue(responseMessage.isStatus());
    }

    @Ve("^Kullanıcı ankete daha önceden cevap vermemiş ise$")
    public void kullanıcıAnketeDahaÖncedenCevapVermemişIse() throws Throwable {
        assertFalse(SurveyUtil.checkIfUserAlreadyAnsweredSurvey
                (answer.getUserId(), answer.getSurveyId(), answerRepository).isStatus());

    }

    @Ve("^En az (\\d+) adet soruya cevap içeriyor ise$")
    public void enAzAdetSoruyaCevapIçeriyorIse(int arg0) throws Throwable {
        assertThat(answer.getAnsweredQuestions().size(), Matchers.greaterThanOrEqualTo(1));
    }

    @Ve("^Verilen her cevap ankette bir soruya karşılık geliyor ise$")
    public void verilenHerCevapAnketteBirSoruyaKarşılıkGeliyorIse() throws Throwable {
        try {
            SurveyUtil.checkAnsweredQuestions(initialData.survey, answer, surveyMessageProvider);
        } catch (GeneralException sExp) {
            fail();
        }
    }

    @Ve("^Anket aktif ise$")
    public void anketAktifIse() throws Throwable {
        assertTrue(initialData.survey.getIsActive());
    }

    @Ozaman("^Anketin katılanların sayısı güncellenir$")
    public void anketinKatılanlarınSayısıGüncellenir() throws Throwable {
        SurveyUtil.updateSurveyVoteCount(answer, initialData.survey);
        Assert.assertNotEquals(initialData.survey.getParticipants(), answeredSurvey.getParticipants());
    }

    @Ve("^Anketin sorularının cevaplanma sayıları güncellenir$")
    public void anketinSorularınınCevaplanmaSayılarıGüncellenir() throws Throwable {

        answer.getAnsweredQuestions().forEach((answeredQuestionId, answeredOption) -> {
            Predicate<Question> questionPredicate;
            questionPredicate = surveyQuestion -> surveyQuestion.getId().equals(answeredQuestionId);

            int totalVotersUpdated = Objects.requireNonNull(initialData.survey.getQuestions()
                    .stream()
                    .filter(questionPredicate).findFirst().orElse(null))
                    .getTotalVoters();

            int totalVotersTemp = Objects.requireNonNull(answeredSurvey.getQuestions()
                    .stream()
                    .filter(questionPredicate).findFirst().orElse(null))
                    .getTotalVoters();

            assertNotEquals(Integer.valueOf(totalVotersUpdated), Integer.valueOf(totalVotersTemp));
        });
    }

    @Ve("^Anketteki soruların seçeneklerinin seçilme sayısı güncellenir$")
    public void ankettekiSorularınSeçeneklerininSeçilmeSayısıGüncellenir() throws Throwable {

        answer.getAnsweredQuestions().forEach((answeredQuestionId, answeredOption) -> {
            Predicate<Question> questionPredicate;
            questionPredicate = surveyQuestion -> surveyQuestion.getId().equals(answeredQuestionId);

            Predicate<QuestionOption> questionOptionPredicate;
            questionOptionPredicate = questionOption -> questionOption.getText().equals(answeredOption);

            int votersTemp = Objects.requireNonNull(
                    Objects.requireNonNull(answeredSurvey.getQuestions()
                            .stream()
                            .filter(questionPredicate)
                            .findFirst().orElse(null))
                            .getOptions()
                            .stream()
                            .filter(questionOptionPredicate)
                            .findFirst().orElse(null))
                    .getVoters();

            int votersUpdated = Objects.requireNonNull(
                    Objects.requireNonNull(initialData.survey.getQuestions()
                            .stream()
                            .filter(questionPredicate)
                            .findFirst().orElse(null))
                            .getOptions()
                            .stream()
                            .filter(questionOptionPredicate)
                            .findFirst().orElse(null))
                    .getVoters();

            assertNotEquals(Integer.valueOf(votersTemp), Integer.valueOf(votersUpdated));

        });
    }

    @Ve("^Sistem cevabı kaydeder$")
    public void sistemCevabıKaydeder() throws Throwable {
        ResponseMessage responseMessage;
        responseMessage = answerService.answerSurvey(answer, initialData.survey.getId(), answer.getEventId());
        String exceptedMessage = initialData.env.getProperty("surveyAnsweredSuccessfully");

        assertEquals(exceptedMessage, responseMessage.getMessage());
        Answer persistedAnswer = (Answer) responseMessage.getReturnObject();
        assertNotEquals(answeredSurvey.hashCode(), surveyRepository.findById((persistedAnswer.getSurveyId())).hashCode());

        assertNotNull(persistedAnswer.getId());
    }

    /* Not Happy ending scenarios */

    @Fakat("^Kullanıcı ankete daha önceden cevap vermiş ise$")
    public void kullanıcıAnketeDahaÖncedenCevapVermişIse() throws Throwable {
        answerService.answerSurvey(answer, initialData.survey.getId(), initialData.event.getId());
        boolean alreadyAnswered = SurveyUtil
                .checkIfUserAlreadyAnsweredSurvey(initialData.user.getId(), initialData.survey.getId(),
                        answerRepository).isStatus();
        assertTrue(alreadyAnswered);
    }

    @Ozaman("^Sistem cevabı kayıt etmez ve kullanıcıya bu anketi daha önce cevapladınız gibi bir uyarı mesajı gönderir$")
    public void sistemCevabıKayıtEtmezVeKullanıcıyaBuAnketiDahaÖnceCevapladınızGibiBirUyarıMesajıGönderir() throws Throwable {

        ResponseMessage responseMessage;

        responseMessage = answerService.answerSurvey(answer, initialData.survey.getId(), answer.getEventId());
        String exceptedMessage = initialData.env.getProperty("surveyAlreadyAnswered");
        assertEquals(exceptedMessage, responseMessage.getMessage());
    }

    @Fakat("^En az (\\d+) adet soruya cevap içermiyor ise$")
    public void enAzAdetSoruyaCevapIçermiyorIse(int arg0) throws Throwable {

        Map<String, String> tempAnsweredQuestions = answer.getAnsweredQuestions();
        answer.getAnsweredQuestions().clear();

        assertEquals(0, answer.getAnsweredQuestions().size());

        answer.setAnsweredQuestions(tempAnsweredQuestions);
    }

    @Ozaman("^Sistem cevabı kayıt etmez ve kullanıcıya en az (\\d+) soru cevaplanmalı şeklinde bir uyarı mesajı gönderir$")
    public void sistemCevabıKayıtEtmezVeKullanıcıyaEnAzSoruCevaplanmalıŞeklindeBirUyarıMesajıGönderir(int arg0) throws Throwable {

        Map<String, String> tempAnsweredQuestions = answer.getAnsweredQuestions();
        answer.getAnsweredQuestions().clear();
        ResponseMessage responseMessage;

        responseMessage = answerService.answerSurvey(answer, initialData.survey.getId(), answer.getEventId());
        String exceptedMessage = initialData.env.getProperty("survey.answers.empty");
        assertTrue(initialData.checkAnswerCreateErrorMessages(exceptedMessage, responseMessage.getMessage()));

        answer.setAnsweredQuestions(tempAnsweredQuestions);
    }


    @Fakat("^Verilen her cevap ankette bir soruya karşılık gelmiyor ise$")
    public void verilenHerCevapAnketteBirSoruyaKarşılıkGelmiyorIse() throws Throwable {

        // wrong question id.
        String questionOption = answer.getAnsweredQuestions().remove(initialData.survey.getQuestions().get(0).getId());
        answer.getAnsweredQuestions().put("notQuestionId", questionOption);
        boolean catched = false;
        try {
            SurveyUtil.checkAnsweredQuestions(initialData.survey, answer, surveyMessageProvider);
        } catch (GeneralException exp) {
            catched = true;
        }
        assertTrue(catched);

        // wrong question option
        catched = false;
        answer.getAnsweredQuestions().remove("notQuestionId");
        answer.getAnsweredQuestions().put(initialData.survey.getQuestions().get(0).getId(), "notQuestionOption");
        try {
            SurveyUtil.checkAnsweredQuestions(initialData.survey, answer, surveyMessageProvider);
        } catch (GeneralException exp) {
            catched = true;
        }
        assertTrue(catched);
        answer.getAnsweredQuestions().put(initialData.survey.getQuestions().get(0).getId(),
                initialData.survey.getQuestions().get(0).getOptions().get(0).getText());
    }

    @Ozaman("^Sistem cevabı kayıt etmez ve kullanıcıya cevapların sorularla uyumsuzluğu ile alakalı bir mesaj yollanır$")
    public void sistemCevabıKayıtEtmezVeKullanıcıyaCevaplarınSorularlaUyumsuzluğuIleAlakalıBirMesajYollanır() throws Throwable {

        // wrong question id.
        String questionOption = answer.getAnsweredQuestions().remove(initialData.survey.getQuestions().get(0).getId());
        answer.getAnsweredQuestions().put("notQuestionId", questionOption);

        try {
            answerService.answerSurvey(answer, initialData.survey.getId(), initialData.event.getId());
        } catch (GeneralException exp) {
            String exceptedMessage = initialData.env.getProperty("questionCanNotFoundWithGivenId");
            assertEquals(exceptedMessage, exp.getMessage());
        }
        // wrong question option
        answer.getAnsweredQuestions().remove("notQuestionId");
        answer.getAnsweredQuestions().put(initialData.survey.getQuestions().get(0).getId(), "notQuestionOption");

        try {
            answerService.answerSurvey(answer, initialData.survey.getId(), initialData.event.getId());
        } catch (GeneralException exp) {
            String exceptedMessage = initialData.env.getProperty("questionAndAnswerMismatch");
            assertEquals(exceptedMessage, exp.getMessage());

        }
    }

}
