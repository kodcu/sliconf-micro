package javaday.istanbul.sliconf.micro.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.ResponseTransformer;

import java.lang.reflect.Type;
import java.util.Map;


/**
 * Created by ttayfur on 7/6/17.
 */
public class JsonUtil {

    private static final Logger logger = LogManager.getLogger(JsonUtil.class);

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static <T> T fromJson(String string, Class<T> clazz) {
        T returnedClass = null;

        try {
            returnedClass = new Gson().fromJson(string, clazz);
        } catch (JsonSyntaxException e) {
            logger.error(e.getMessage(), e);
        }
        return returnedClass;
    }

    public static Map<String, Object> mapFromObject(Object object) {
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> myMap = new Gson().fromJson(toJson(object), type);
        return myMap;
    }

    public static ResponseTransformer json() {
        return JsonUtil::toJson;
    }
}