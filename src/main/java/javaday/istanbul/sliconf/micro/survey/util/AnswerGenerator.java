package javaday.istanbul.sliconf.micro.survey.util;

import com.devskiller.jfairy.Fairy;
import com.google.common.collect.Maps;
import javaday.istanbul.sliconf.micro.survey.model.Answer;
import javaday.istanbul.sliconf.micro.survey.model.Survey;
import javaday.istanbul.sliconf.micro.user.UserGenerator;
import javaday.istanbul.sliconf.micro.user.model.User;

import java.util.*;

/*** Anketlere rastgele cevaplar uretmek icin kullanilan bir sinif. */

public class AnswerGenerator {

    private AnswerGenerator() {
    }

    public static List<Answer> generateRandomAnswersToSurvey(Survey survey, Set<User> users) {

        List<Answer> answers = new ArrayList<>();

        users.forEach(user -> {
            Answer answer = Answer.builder()
                    .eventId(survey.getEventId())
                    .eventKey(survey.getEventKey())
                    .surveyId(survey.getId())
                    .userId(user.getId()).build();

            Map<String, String> answeredQuestions = new HashMap<>();
            Fairy fairy = Fairy.create();
            int randomAnsweredQuestionCount = fairy.baseProducer().randomBetween(1, survey.getQuestions().size());

            for (int j = 0; j < randomAnsweredQuestionCount; j++) {

                String id = survey.getQuestions().get(j).getId();
                int randomQuestionOptionIndex = fairy.baseProducer()
                        .randomBetween(0, survey.getQuestions().get(j).getOptions().size() - 1);
                String option = survey.getQuestions().get(j).getOptions().get(randomQuestionOptionIndex).getText();

                answeredQuestions.put(id, option);
            }
            answer.setAnsweredQuestions(Maps.newHashMap(answeredQuestions));
            answers.add(answer);

            answeredQuestions.clear();
        });
        return answers;
    }
}
