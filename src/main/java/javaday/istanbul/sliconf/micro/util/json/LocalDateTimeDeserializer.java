package javaday.istanbul.sliconf.micro.util.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        return Instant.ofEpochMilli(jsonElement.getAsJsonPrimitive().getAsLong()).atZone(ZoneId.of("Asia/Istanbul")).toLocalDateTime();

    }
}
