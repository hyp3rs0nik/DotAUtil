package com.hamaksoftware.mydota.gson.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.hamaksoftware.mydota.enums.PersonaState;

import java.lang.reflect.Type;

public class PersonaStateDeserializer implements JsonDeserializer<PersonaState> {
    @Override
    public PersonaState deserialize(JsonElement element, Type t, JsonDeserializationContext c) throws JsonParseException {
        int key = element.getAsInt();
        return PersonaState.fromKey(key);
    }
}
