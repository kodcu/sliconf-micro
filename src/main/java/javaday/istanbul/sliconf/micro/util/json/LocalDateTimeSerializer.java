package javaday.istanbul.sliconf.micro.util.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
    @Override
    public JsonElement serialize(LocalDateTime dateTime, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(dateTime.atZone(ZoneId.of("Asia/Istanbul")).toInstant().toEpochMilli());
    }
}
