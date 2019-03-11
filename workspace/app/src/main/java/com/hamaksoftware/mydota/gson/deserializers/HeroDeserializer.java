package com.hamaksoftware.mydota.gson.deserializers;

import com.activeandroid.query.Select;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.hamaksoftware.mydota.activeandroid.model.Hero;
import com.hamaksoftware.mydota.enums.HeroType;

import java.lang.reflect.Type;

public class HeroDeserializer implements JsonDeserializer<Hero> {
    @Override
    public Hero deserialize(JsonElement element, Type t, JsonDeserializationContext c) throws JsonParseException {
        if(element.isJsonObject()){
            final JsonObject json = element.getAsJsonObject();
            Hero hero = new Hero();
            hero.localizedName = json.get("localizedName").getAsString();
            hero.type = HeroType.fromKey(json.get("type").getAsInt());
            hero.heroId = json.get("heroId").getAsInt();
            hero.name = json.get("name").getAsString();
            return hero;
        }else if(element.isJsonPrimitive()){
            int heroId = element.getAsInt();
            System.out.println("hid: "+heroId);
            Hero  hero =  new Select().from(Hero.class).where("heroId=?", heroId).executeSingle();
            return hero;
        }
        return null;

    }
}
