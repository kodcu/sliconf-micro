package javaday.istanbul.sliconf.micro.steps.survey;

import cucumber.api.java.tr.Fakat;
import cucumber.api.java.tr.Ozaman;
import javaday.istanbul.sliconf.micro.SpringBootTestConfig;
import javaday.istanbul.sliconf.micro.survey.model.Question;
import javaday.istanbul.sliconf.micro.survey.model.QuestionOption;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@Ignore
public class AAA7 extends SpringBootTestConfig { // NOSONAR

    @Autowired
    private InitialData initialData;

    @Fakat("^Etkinlik sahibi anketteki sorularin ismini boş bırakmış ise$")
    public void etkinlikSahibiAnkettekiSorularinIsminiBoşBırakmışIse() throws Throwable {

        Question newQuestion = Question.builder().build();
        newQuestion.setText(null);
        newQuestion.setId("question2Id");
        List<Question> questions = new ArrayList<>();
        questions.add(newQuestion);
        QuestionOption questionOption1 = QuestionOption.builder().build();
        QuestionOption questionOption2 = QuestionOption.builder().build();
        questionOption1.setText("Seçenek 01");
        questionOption1.setId("questionOption1Id");
        questionOption2.setText("Seçenek 02");
        questionOption2.setId("questionOption2Id");
        List<QuestionOption> questionOptions = new ArrayList<>();
        questionOptions.add(questionOption1);
        questionOptions.add(questionOption2);
        newQuestion.setOptions(questionOptions);
        initialData.survey.setQuestions(questions);

        initialData.survey.getQuestions()
                .forEach(question -> assertThat(question.getText(), isEmptyOrNullString()));
    }

    @Ozaman("^Sistem anketi kayıt etmez ve ön tarafa soruların isimleri boş olamaz gibi bir hata mesajı gönderilir$")
    public void sistemAnketiKayıtEtmezVeÖnTarafaSorularınIsimleriBoşOlamazGibiBirHataMesajıGönderilir() throws Throwable {

        String exceptedErrorMessage = initialData.env.getProperty("survey.question.text.blank");
        assertTrue(initialData.checkSurveyCreateErrorMessages(exceptedErrorMessage, initialData.survey));

    }
}
