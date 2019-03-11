package com.hamaksoftware.mydota.gson.deserializers;

import com.activeandroid.query.Select;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.hamaksoftware.mydota.activeandroid.model.Hero;
import com.hamaksoftware.mydota.activeandroid.model.Item;

import java.lang.reflect.Type;

public class ItemDeserializer implements JsonDeserializer<Item> {
    @Override
    public Item deserialize(JsonElement element, Type t, JsonDeserializationContext c) throws JsonParseException {
        int itemId = element.getAsInt();

        return new Select().from(Item.class)
                .where("itemId=?", itemId).executeSingle();
    }
}
