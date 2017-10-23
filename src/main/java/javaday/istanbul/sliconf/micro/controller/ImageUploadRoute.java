package javaday.istanbul.sliconf.micro.controller;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
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
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

import static spark.Spark.staticFiles;

@Api
@javax.ws.rs.Path("/service/image/upload")
@Produces("application/json")
@Component
public class ImageUploadRoute implements Route {

    private File uploadDir = new File("upload");

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

        uploadDir.mkdir(); // create the upload directory if it doesn't exist

        staticFiles.externalLocation("upload");


        Path tempFile = Files.createTempFile(uploadDir.toPath(), "", "");

        request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

        try (InputStream input = request.raw().getPart("uploaded_file").getInputStream()) { // getPart needs to use same "name" as input field in form
            Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        // logInfo(request, tempFile);
        // return "<h1>You uploaded this image:<h1><img src='" + tempFile.getFileName() + "'>";

        responseMessage.setStatus(true);
        responseMessage.setMessage("Image uploaded successfully");
        responseMessage.setReturnObject(tempFile.getFileName().toString());

        return responseMessage;
    }
}
