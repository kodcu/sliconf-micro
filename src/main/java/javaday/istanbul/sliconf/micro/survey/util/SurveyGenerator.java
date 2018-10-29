package javaday.istanbul.sliconf.micro.survey.util;

import com.devskiller.jfairy.Fairy;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.survey.model.Question;
import javaday.istanbul.sliconf.micro.survey.model.QuestionOption;
import javaday.istanbul.sliconf.micro.survey.model.Survey;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/*** Rastgele anket uretmek icin kullanilan bir sinif. */
public class SurveyGenerator {

    private SurveyGenerator() {
    }

    public static Survey buildFromOther(Survey survey) {

        List<Question> questionList = new ArrayList<>();
        survey.getQuestions().forEach(question -> {
            Question newQuestion = Question.builder()
                    .text(question.getText())
                    .totalVoters(question.getTotalVoters())
                    .options(new ArrayList<>(question.getOptions()))
                    .id(question.getId()).build();
            questionList.add(newQuestion);
        });
        return Survey.builder()
                .eventId(survey.getId())
                .eventKey(survey.getEventKey())
                .name(survey.getName())
                .userId(survey.getUserId())
                .description(survey.getDescription())
                .endTime(survey.getEndTime())
                .startTime(survey.getStartTime())
                .participants(survey.getParticipants())
                .questions(questionList)
                .build();
    }

    public static List<Survey> generateRandomSurveys(int count, Event event, String userId) {

        Fairy fairy = Fairy.create();
        List<Question> questionList = new ArrayList<>();
        List<Survey> surveyList = new ArrayList<>();
        // generate surveys
        for (int i = 0; i < count; i++) {
            // generate questions
            for (int k = 0; k < fairy.baseProducer().randomBetween(1, 20); k++) {
                Question question = Question.builder()
                        .text(fairy.textProducer().latinSentence(20))
                        .id(new ObjectId().toString())
                        .options(new ArrayList<>())
                        .totalVoters(0)
                        .build();
                //generate question options.
                for (int j = 0; j < fairy.baseProducer().randomBetween(2, 5); j++) {
                    QuestionOption questionOption = QuestionOption.builder()
                            .text(fairy.textProducer().latinSentence(20))
                            .id(new ObjectId().toString())
                            .voters(0)
                            .build();
                    question.getOptions().add(questionOption);
                }

                questionList.add(question);
            }
            LocalDateTime time = fairy
                    .dateProducer()
                    .randomDateBetweenTwoDates(LocalDateTime.now().plusWeeks(1), LocalDateTime.now().plusYears(1));

            Survey survey = Survey.builder()
                    .id(new ObjectId().toString())
                    .description(fairy.textProducer().latinSentence(300))
                    .eventKey(event.getKey())
                    .eventId(event.getId())
                    .name(fairy.textProducer().latinSentence(20))
                    .userId(userId)
                    .participants(0)
                    .viewers(0)
                    .viewerList(new ArrayList<>())
                    .startTime(String.valueOf(time.toEpochSecond(ZoneOffset.UTC)))
                    .endTime(String.valueOf(fairy.dateProducer()
                            .randomDateBetweenTwoDates(time, time.plusWeeks(1)).toEpochSecond(ZoneOffset.UTC)))
                    .build();

            survey.setQuestions(questionList);
            surveyList.add(survey);
            questionList = new ArrayList<>();
        }

        return surveyList;
    }

}
