package javaday.istanbul.sliconf.micro.controller.event;

public class CreateEventController {
    /*

    private final EventDao eventDao = new EventDao();

    private final EventControllerMessageProvider messageProvider = EventControllerMessageProvider.instance();

    public ResponseMessage createUser(Request request, Response response) {

        // Todo event olusturma kurallari belirlenmeli

        ResponseMessage responseMessage;

        String body = request.body();
        Event event = JsonUtil.fromJson(body, Event.class);

        Event dbEvent = eventDao.getEvent(event);

        // eger user yoksa kayit et
        if (Objects.nonNull(dbEvent)) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventAlreadyRegistered"), new Object());
            return responseMessage;
        }

        // todo yazilip yazilmadigini kontrol et
        ResponseMessage dbResponse = eventDao.saveEvent(event);

        if (!dbResponse.isStatus()) {
            return dbResponse;
        }

        responseMessage = new ResponseMessage(true,
                messageProvider.getMessage("eventCreatedSuccessfully"), event);

        return responseMessage;
    }
    */

}
