package javaday.istanbul.sliconf.micro.util;

import com.google.gson.Gson;
import spark.ResponseTransformer;


/**
 * Created by ttayfur on 7/6/17.
 */
public class JsonUtil {

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static ResponseTransformer json() {
        return JsonUtil::toJson;
    }
}