package com.hamaksoftware.mydota.enums;
public enum PersonaState {
    PRIVATE_OFFLINE(0),ONLINE(1),BUSY(2),AWAY(3),SNOOZE(4),LOOKING_TO_TRADE(5),LOOKING_TO_PLAY(6);

    private final int key;
    private PersonaState(int code){
        this.key = code;
    }
    public int getKey(){return key;}

    public static PersonaState fromKey(int key) {
        for(PersonaState type : PersonaState.values()) {
            if(type.getKey() == key) {
                return type;
            }
        }
        return null;
    }

}
