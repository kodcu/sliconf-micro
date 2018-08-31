package javaday.istanbul.sliconf.micro.controller;

import javaday.istanbul.sliconf.micro.controller.admin.AdminChangeEventStateForEventRoute;
import javaday.istanbul.sliconf.micro.controller.admin.AdminListEventStatesRoute;
import javaday.istanbul.sliconf.micro.controller.admin.AdminListEventsRoute;
import javaday.istanbul.sliconf.micro.controller.admin.AdminListUsersRoute;
import javaday.istanbul.sliconf.micro.controller.event.*;
import javaday.istanbul.sliconf.micro.controller.event.agenda.CreateAgendaRoute;
import javaday.istanbul.sliconf.micro.controller.event.agenda.GetVoteAgendaElementRoute;
import javaday.istanbul.sliconf.micro.controller.event.agenda.VoteAgendaElementRoute;
import javaday.istanbul.sliconf.micro.controller.event.comment.AddNewCommentRoute;
import javaday.istanbul.sliconf.micro.controller.event.comment.ListCommentsRoute;
import javaday.istanbul.sliconf.micro.controller.event.comment.ModerateCommentRoute;
import javaday.istanbul.sliconf.micro.controller.event.comment.VoteCommentRoute;
import javaday.istanbul.sliconf.micro.controller.event.floor.CreateFloorRoute;
import javaday.istanbul.sliconf.micro.controller.event.room.CreateRoomRoute;
import javaday.istanbul.sliconf.micro.controller.event.schedule.AddToScheduleRoute;
import javaday.istanbul.sliconf.micro.controller.event.schedule.ListScheduleRoute;
import javaday.istanbul.sliconf.micro.controller.event.schedule.RemoveFromScheduleRoute;
import javaday.istanbul.sliconf.micro.controller.event.speaker.CreateSpeakerRoute;
import javaday.istanbul.sliconf.micro.controller.event.sponsor.CreateSponsorRoute;
import javaday.istanbul.sliconf.micro.controller.login.*;

import javaday.istanbul.sliconf.micro.survey.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


// all arg const ile beanlar otomatik olarak constructor injection edilebilir.
@AllArgsConstructor
@Component
public class RouteObjects {

    // User related routes
    public final CreateUserRoute createUserRoute;
    public final CreateUserAnonymousRoute createUserAnonymousRoute;

    public final LoginUserRoute loginUserRoute;
    public final LoginUserAnonymousRoute loginUserAnonymousRoute;
    public final LoginUserAuthRoute loginUserAuthRoute;

    public final SendPasswordResetRoute sendPasswordResetRoute;
    public final ResetPasswordRoute resetPasswordRoute;
    public final UpdateUserRoute updateUserRoute;

    // Event related routes
    public final CreateEventRoute createEventRoute;
    public final DeleteEventRoute deleteEventRoute;
    public final GetEventWithKeyRoute getEventWithKeyRoute;
    public final ListEventsRoute listEventsRoute;
    public final GetStatisticsRoute getStatisticsRoute;

    public final CreateFloorRoute createFloorRoute;

    public final CreateRoomRoute createRoomRoute;

    public final CreateSponsorRoute createSponsorRoute;

    public final CreateSpeakerRoute createSpeakerRoute;

    public final CreateAgendaRoute createAgendaRoute;
    public final VoteAgendaElementRoute voteAgendaElementRoute;
    public final GetVoteAgendaElementRoute getVoteAgendaElementRoute;

    public final AddNewCommentRoute addNewCommentRoute;
    public final ListCommentsRoute listCommentsRoute;
    public final ModerateCommentRoute moderateCommentRoute;
    public final VoteCommentRoute voteCommentRoute;

    public final AddNewSurveyRoute addNewSurveyRoute;
    public final RemoveSurveyRoute removeSurveyRoute;
    public final AnswerSurveyRoute answerSurveyRoute;
    public final GetSurveyRoute getSurveyRoute;
    public final UpdateSurveyRoute updateSurveyRoute;

    //
    public final ImageUploadRoute imageUploadRoute;
    public final ImageGetRoute imageGetRoute;

    public final AddToScheduleRoute addToScheduleRoute;
    public final RemoveFromScheduleRoute removeFromScheduleRoute;
    public final ListScheduleRoute listScheduleRoute;

    public final AdminListUsersRoute adminListUsersRoute;
    public final AdminListEventsRoute adminListEventsRoute;
    public final AdminListEventStatesRoute adminListEventStatesRoute;
    public final AdminChangeEventStateForEventRoute adminChangeEventStateForEventRoute;


//    @Autowired
//    public RouteObjects(BeanFactory beanFactory) {
//        createUserRoute = beanFactory.getBean(CreateUserRoute.class);
//        createUserAnonymousRoute = beanFactory.getBean(CreateUserAnonymousRoute.class);
//
//        loginUserRoute = beanFactory.getBean(LoginUserRoute.class);
//        loginUserAnonymousRoute = beanFactory.getBean(LoginUserAnonymousRoute.class);
//        loginUserAuthRoute = beanFactory.getBean(LoginUserAuthRoute.class);
//
//        sendPasswordResetRoute = beanFactory.getBean(SendPasswordResetRoute.class);
//        resetPasswordRoute = beanFactory.getBean(ResetPasswordRoute.class);
//        updateUserRoute = beanFactory.getBean(UpdateUserRoute.class);
//
//        createEventRoute = beanFactory.getBean(CreateEventRoute.class);
//        deleteEventRoute = beanFactory.getBean(DeleteEventRoute.class);
//        getEventWithKeyRoute = beanFactory.getBean(GetEventWithKeyRoute.class);
//        listEventsRoute = beanFactory.getBean(ListEventsRoute.class);
//        getStatisticsRoute = beanFactory.getBean(GetStatisticsRoute.class);
//
//
//        imageUploadRoute = beanFactory.getBean(ImageUploadRoute.class);
//        imageGetRoute = beanFactory.getBean(ImageGetRoute.class);
//
//        createFloorRoute = beanFactory.getBean(CreateFloorRoute.class);
//
//        createRoomRoute = beanFactory.getBean(CreateRoomRoute.class);
//
//        createSponsorRoute = beanFactory.getBean(CreateSponsorRoute.class);
//
//        createSpeakerRoute = beanFactory.getBean(CreateSpeakerRoute.class);
//
//        createAgendaRoute = beanFactory.getBean(CreateAgendaRoute.class);
//        voteAgendaElementRoute = beanFactory.getBean(VoteAgendaElementRoute.class);
//        getVoteAgendaElementRoute = beanFactory.getBean(GetVoteAgendaElementRoute.class);
//
//        addNewCommentRoute = beanFactory.getBean(AddNewCommentRoute.class);
//        listCommentsRoute = beanFactory.getBean(ListCommentsRoute.class);
//        moderateCommentRoute = beanFactory.getBean(ModerateCommentRoute.class);
//        voteCommentRoute = beanFactory.getBean(VoteCommentRoute.class);
//
//        addToScheduleRoute = beanFactory.getBean(AddToScheduleRoute.class);
//        removeFromScheduleRoute = beanFactory.getBean(RemoveFromScheduleRoute.class);
//        listScheduleRoute = beanFactory.getBean(ListScheduleRoute.class);
//
//        adminListUsersRoute = beanFactory.getBean(AdminListUsersRoute.class);
//        adminListEventsRoute = beanFactory.getBean(AdminListEventsRoute.class);
//        adminListEventStatesRoute = beanFactory.getBean(AdminListEventStatesRoute.class);
//        adminChangeEventStateForEventRoute = beanFactory.getBean(AdminChangeEventStateForEventRoute.class);
//    }
}
