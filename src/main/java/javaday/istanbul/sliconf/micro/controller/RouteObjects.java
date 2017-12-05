package javaday.istanbul.sliconf.micro.controller;

import javaday.istanbul.sliconf.micro.controller.event.CreateEventRoute;
import javaday.istanbul.sliconf.micro.controller.event.GetEventWithKeyRoute;
import javaday.istanbul.sliconf.micro.controller.event.ListEventsRoute;
import javaday.istanbul.sliconf.micro.controller.event.agenda.CreateAgendaRoute;
import javaday.istanbul.sliconf.micro.controller.event.floor.CreateFloorRoute;
import javaday.istanbul.sliconf.micro.controller.event.room.CreateRoomRoute;
import javaday.istanbul.sliconf.micro.controller.event.speaker.CreateSpeakerRoute;
import javaday.istanbul.sliconf.micro.controller.event.sponsor.CreateSponsorRoute;
import javaday.istanbul.sliconf.micro.controller.login.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RouteObjects {

    // User related routes
    public final CreateUserRoute createUserRoute;

    public final LoginUserRoute loginUserRoute;

    public final SendPasswordResetRoute sendPasswordResetRoute;
    public final ResetPasswordRoute resetPasswordRoute;
    public final UpdateUserRoute updateUserRoute;

    // Event related routes
    public final CreateEventRoute createEventRoute;
    public final GetEventWithKeyRoute getEventWithKeyRoute;
    public final ListEventsRoute listEventsRoute;

    public final CreateFloorRoute createFloorRoute;

    public final CreateRoomRoute createRoomRoute;

    public final CreateSponsorRoute createSponsorRoute;

    public final ImageUploadRoute imageUploadRoute;
    public final ImageGetRoute imageGetRoute;

    public final CreateSpeakerRoute createSpeakerRoute;

    public final CreateAgendaRoute createAgendaRoute;


    @Autowired
    public RouteObjects(BeanFactory beanFactory) {
        createUserRoute = beanFactory.getBean(CreateUserRoute.class);
        loginUserRoute = beanFactory.getBean(LoginUserRoute.class);
        sendPasswordResetRoute = beanFactory.getBean(SendPasswordResetRoute.class);
        resetPasswordRoute = beanFactory.getBean(ResetPasswordRoute.class);
        updateUserRoute = beanFactory.getBean(UpdateUserRoute.class);

        createEventRoute = beanFactory.getBean(CreateEventRoute.class);
        getEventWithKeyRoute = beanFactory.getBean(GetEventWithKeyRoute.class);
        listEventsRoute = beanFactory.getBean(ListEventsRoute.class);


        imageUploadRoute = beanFactory.getBean(ImageUploadRoute.class);
        imageGetRoute = beanFactory.getBean(ImageGetRoute.class);

        createFloorRoute = beanFactory.getBean(CreateFloorRoute.class);

        createRoomRoute = beanFactory.getBean(CreateRoomRoute.class);

        createSponsorRoute = beanFactory.getBean(CreateSponsorRoute.class);

        createSpeakerRoute = beanFactory.getBean(CreateSpeakerRoute.class);

        createAgendaRoute = beanFactory.getBean(CreateAgendaRoute.class);
    }
}
