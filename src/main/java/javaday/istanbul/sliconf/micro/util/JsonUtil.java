package javaday.istanbul.sliconf.micro.util;


import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ResponseTransformer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Map;


/**
 * Created by ttayfur on 7/6/17.
 */
public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class,
                    (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext)
                            -> LocalDateTime.parse(json.getAsJsonPrimitive().getAsString()))
            .create();

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T fromJson(String string, Class<T> clazz) {
        T returnedClass = null;

        try {
            returnedClass = gson.fromJson(string, clazz);
        } catch (JsonSyntaxException e) {
            logger.error(e.getMessage(), e);
        }
        return returnedClass;
    }

    public static Map<String, Object> mapFromObject(Object object) {
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();

        return gson.fromJson(toJson(object), type);
    }

    public static ResponseTransformer json() {
        return JsonUtil::toJson;
    }
}