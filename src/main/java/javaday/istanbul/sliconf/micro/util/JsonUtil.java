package javaday.istanbul.sliconf.micro.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javaday.istanbul.sliconf.micro.model.User;
import spark.ResponseTransformer;

import java.lang.reflect.Type;
import java.util.Map;


/**
 * Created by ttayfur on 7/6/17.
 */
public class JsonUtil {

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static Object fromJson(String string) {
        return new Gson().fromJson(string, User.class);
    }

    public static Map<String, Object> mapFromObject(Object object) {
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> myMap = new Gson().fromJson( toJson(object), type);
        return myMap;
    }

    public static ResponseTransformer json() {
        return JsonUtil::toJson;
    }
}