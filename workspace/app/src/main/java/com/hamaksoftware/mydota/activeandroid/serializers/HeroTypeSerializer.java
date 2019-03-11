package com.hamaksoftware.mydota.activeandroid.serializers;

import com.activeandroid.serializer.TypeSerializer;
import com.hamaksoftware.mydota.enums.HeroType;

final public class HeroTypeSerializer extends TypeSerializer {
    @Override
    public Class<?> getDeserializedType() {
        return HeroType.class;
    }

    @Override
    public Class<?> getSerializedType() {
        return HeroType.class;
    }


    /*
    @Override
    public SerializedType getSerializedType() {
        return SerializedType.INTEGER;
    }
    */

    @Override
    public Integer serialize(Object o) {
        if(o != null){
            return ((HeroType)o).getKey();
        }
        return null;
    }

    @Override
    public HeroType deserialize(Object o) {
        if(o == null) {
            return null;
        }

        return HeroType.fromKey(((HeroType)o).getKey());
    }
}
