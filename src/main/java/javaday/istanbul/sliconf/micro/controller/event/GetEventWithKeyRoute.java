package javaday.istanbul.sliconf.micro.controller.event;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.event.TotalUser;
import javaday.istanbul.sliconf.micro.model.event.TotalUsers;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.EventControllerMessageProvider;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Api
@Path("/service/events/get/with-key/:key")
@Produces("application/json")
@Component
public class GetEventWithKeyRoute implements Route {

    private EventControllerMessageProvider messageProvider;
    private EventRepositoryService repositoryService;
    private UserRepositoryService userRepositoryService;

    @Autowired
    public GetEventWithKeyRoute(EventControllerMessageProvider messageProvider,
                                EventRepositoryService eventRepositoryService,
                                UserRepositoryService userRepositoryService) {
        this.messageProvider = messageProvider;
        this.repositoryService = eventRepositoryService;
        this.userRepositoryService = userRepositoryService;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GET
    @ApiOperation(value = "Returns event with given key", nickname = "GetEventWithKeyRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "key", paramType = "path"),
            @ApiImplicitParam(dataType = "string", name = "userId", paramType = "query"),
            @ApiImplicitParam(dataType = "string", name = "statistic", paramType = "query", allowableValues = "true"),
    })
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {

        String key = request.params("key");
        String userId = request.queryParams("userId");
        String statistic = request.queryParams("statistic");

        return getEventWithKey(key, userId, statistic);
    }

    public ResponseMessage getEventWithKey(String key, String userId, String statistic) {
        // event var mı diye kontrol et
        if (Objects.isNull(key)) {
            return new ResponseMessage(false,
                    messageProvider.getMessage("eventKeyCantBeEmpty"), new Object());
        }

        key = key.trim();
        Event event = repositoryService.findEventByKeyEquals(key);

        if (Objects.isNull(event)) {
            return new ResponseMessage(false,
                    messageProvider.getMessage("eventCanNotFound"), new Object());
        }

        this.addUserToEvent(event, userId);

        repositoryService.save(event);

        if (Objects.isNull(statistic) || !"true".equals(statistic)) {
            event.setTotalUsers(null);
        }

        return new ResponseMessage(true,
                messageProvider.getMessage("eventFetchedSuccessfully"), event);
    }

    /**
     * Event istatistigi icin gerekli olan,eventi kac kisi ziyaret etmis bilgisi icin
     * Event uzerinde gerekli islemleri yapan end point
     *
     * @param event
     * @param userId
     * @return
     */
    private synchronized ResponseMessage addUserToEvent(Event event, String userId) {
        if (Objects.nonNull(event)) {
            if (Objects.isNull(event.getTotalUsers())) {
                TotalUsers totalUsers = new TotalUsers();

                totalUsers.setAllFetched(0);
                totalUsers.setUniqueCount(0);
                totalUsers.setUsers(new ArrayList<>());

                event.setTotalUsers(totalUsers);
            }

            if (Objects.nonNull(userId) && !userId.isEmpty() && !isUserAlreadyInTotalUserList(event.getTotalUsers().getUsers(), userId)) {
                User user = userRepositoryService.findById(userId).orElse(null);
                if (Objects.nonNull(user)) {
                    TotalUser totalUser = new TotalUser();

                    totalUser.setId(userId);
                    totalUser.setAnonymous(user.getAnonymous());
                    totalUser.setDeviceId(user.getDeviceId());
                    totalUser.setEmail(user.getEmail());
                    totalUser.setFullname(user.getFullname());
                    totalUser.setUsername(user.getUsername());

                    event.getTotalUsers().getUsers().add(totalUser);

                    event.getTotalUsers().setUniqueCount(event.getTotalUsers().getUsers().size());
                }
            }

            event.getTotalUsers().setAllFetched(event.getTotalUsers().getAllFetched() + (long) 1);
            return new ResponseMessage(true, "Total users updated", null);
        }

        return new ResponseMessage(false, "Total users can't updated", null);
    }

    /**
     * Kullanici daha once bu eventi goruntulemis mi goruntulememis mi kontrolu yapiliyor
     *
     * @param users
     * @param userId
     * @return
     */
    private boolean isUserAlreadyInTotalUserList(List<TotalUser> users, String userId) {
        return Objects.nonNull(users) && Objects.nonNull(userId) &&
                users.stream().anyMatch(user -> Objects.nonNull(user) && Objects.nonNull(user.getId()) && user.getId().equals(userId));
    }
}
