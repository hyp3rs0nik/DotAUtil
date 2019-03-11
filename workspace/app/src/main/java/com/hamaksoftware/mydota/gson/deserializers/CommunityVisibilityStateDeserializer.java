package com.hamaksoftware.mydota.gson.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.hamaksoftware.mydota.enums.CommunityVisibilityState;
import java.lang.reflect.Type;

public class CommunityVisibilityStateDeserializer implements JsonDeserializer<CommunityVisibilityState>{
    @Override
    public CommunityVisibilityState deserialize(JsonElement element, Type t, JsonDeserializationContext c) throws JsonParseException {
        int key = element.getAsInt();
        return CommunityVisibilityState.fromKey(key);
    }
}
