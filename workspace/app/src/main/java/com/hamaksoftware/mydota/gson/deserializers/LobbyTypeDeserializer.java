package com.hamaksoftware.mydota.gson.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.hamaksoftware.mydota.enums.LobbyType;

import java.lang.reflect.Type;

/**
 * Created by JV on 6/21/2014.
 */
public class LobbyTypeDeserializer implements JsonDeserializer<LobbyType> {

    @Override
    public LobbyType deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        int key = element.getAsInt();
        return LobbyType.fromKey(key);
    }
}
