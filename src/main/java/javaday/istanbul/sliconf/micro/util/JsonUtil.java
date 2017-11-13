package javaday.istanbul.sliconf.micro.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import javaday.istanbul.sliconf.micro.util.json.LocalDateTimeDeserializer;
import javaday.istanbul.sliconf.micro.util.json.LocalDateTimeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ResponseTransformer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;


/**
 * Created by ttayfur on 7/6/17.
 */
public class JsonUtil {

    private JsonUtil() {
        // private constructor for static
    }

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
            .create();

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T fromJson(String string, Class<T> clazz) {
        T returnedClass = null;

        try {
            if (Objects.nonNull(string) && Objects.nonNull(clazz)) {
                returnedClass = gson.fromJson(string, clazz);
            }
        } catch (JsonSyntaxException e) {
            logger.error(e.getMessage(), e);
        }

        return returnedClass;
    }

    public static Map<String, Object> mapFromObject(Object object) {
        Type type = new TypeToken<Map<String, Object>>() {}.getType();

        return gson.fromJson(toJson(object), type);
    }

    public static ResponseTransformer json() {
        return JsonUtil::toJson;
    }
}