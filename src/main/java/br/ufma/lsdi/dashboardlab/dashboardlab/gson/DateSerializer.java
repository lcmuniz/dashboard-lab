package br.ufma.lsdi.dashboardlab.dashboardlab.gson;

import com.google.gson.*;
import lombok.val;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateSerializer implements JsonDeserializer<LocalDateTime>, JsonSerializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
        try {
            val date = element.getAsString();
            val f = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            return LocalDateTime.parse(date,f);
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
        try {
            return new JsonPrimitive(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }
}