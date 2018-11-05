package javaday.istanbul.sliconf.micro.survey;

import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.survey.model.Survey;
import javaday.istanbul.sliconf.micro.survey.service.SurveyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class SurveyScheduledJobs {

    private final SurveyService surveyService;
    private final EventRepositoryService eventRepositoryService;
    // Her yarim saatte bir anketleri aktif veya passive yapar.
    @Scheduled(fixedRate = 1800000, initialDelay = 1000)
    public void updateSurveysStatus() {
        log.info("survey status update started");
        List<Event> events = eventRepositoryService.findAll();
        List<Survey>  surveys = new ArrayList<>();
        events.forEach(event -> {
            try {
                surveys.addAll((List<Survey>)surveyService.getSurveys(event.getKey()).getReturnObject());
            } catch (Exception e) {
                log.info(e.getMessage(), e.getCause());
            }
            surveys.forEach(survey -> {
                LocalDateTime surveyStartTime = LocalDateTime.
                        ofEpochSecond(Long.valueOf(survey.getStartTime()),1, ZoneOffset.UTC);
                if(surveyStartTime.isBefore(LocalDateTime.now())) {
                    survey.setIsActive(true);
                    surveyService.updateSurvey(survey, event.getKey());
                }
                // TODO: 05.11.2018 Bitmis eventlerin anketleri passive yapilabilir.
            });
            surveys.clear();
        });

    }
}
