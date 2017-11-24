package javaday.istanbul.sliconf.micro.controller;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.util.Constants;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.servlet.MultipartConfigElement;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static spark.Spark.staticFiles;

@Api
@javax.ws.rs.Path("/service/image/upload")
@Produces("application/json")
@Component
public class ImageUploadRoute implements Route {


    @POST
    @ApiOperation(value = "Saves given image and returns image id", nickname = "ImageUploadRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "uploaded_file", paramType = "form") //
    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {

        ResponseMessage responseMessage = new ResponseMessage(false, "Image can not uploaded", "");

        LocalDate now = LocalDate.now();

        String fileName = now.format(DateTimeFormatter.ofPattern("YYYY_MM_dd")).concat(UUID.randomUUID().toString());

        File uploadDir = new File("upload/" + fileName);

        new File("upload").mkdir();

        staticFiles.externalLocation("upload");

        request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

        try (InputStream input = request.raw().getPart("uploaded_file").getInputStream()) { // getPart needs to use same "name" as input field in form

            if (Constants.MAX_UPLOADED_FILE_SIZE < input.available()) {
                responseMessage.setStatus(false);
                responseMessage.setMessage("Image size must be less than 3MB");
                responseMessage.setReturnObject("");
                return responseMessage;
            }

            Files.copy(input, uploadDir.toPath(), StandardCopyOption.REPLACE_EXISTING);
            input.close();
        }

        responseMessage.setStatus(true);
        responseMessage.setMessage("Image uploaded successfully");
        responseMessage.setReturnObject(uploadDir.getName());

        return responseMessage;
    }
}
