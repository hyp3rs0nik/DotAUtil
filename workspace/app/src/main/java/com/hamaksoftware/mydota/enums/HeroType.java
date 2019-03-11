package com.hamaksoftware.mydota.enums;
public enum HeroType {
    AGI(1),INT(2),STR(3);

    private final int key;
    private HeroType(int code){
        this.key = code;
    }
    public int getKey(){return key;}

    public static HeroType fromKey(int key) {
        for(HeroType type : HeroType.values()) {
            if(type.getKey() == key) {
                return type;
            }
        }
        return null;
    }

}
