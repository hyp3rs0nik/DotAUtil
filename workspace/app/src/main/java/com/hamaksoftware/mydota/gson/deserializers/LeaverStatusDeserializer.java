package com.hamaksoftware.mydota.gson.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.hamaksoftware.mydota.enums.LeaverStatus;
import com.hamaksoftware.mydota.enums.LobbyType;

import java.lang.reflect.Type;
public class LeaverStatusDeserializer implements JsonDeserializer<LeaverStatus> {
    @Override
    public LeaverStatus deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        int key = element.getAsInt();
        return LeaverStatus.fromKey(key);
    }
}
