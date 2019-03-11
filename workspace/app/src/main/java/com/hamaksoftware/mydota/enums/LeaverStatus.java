package com.hamaksoftware.mydota.enums;
public enum LeaverStatus {
    NONE(0,"Finished match, no abandon"),
    DISCONNECTED(1,"Player DC, no abandon"),
    DISCONNECTED_TOO_LONG(2,"Player DC > 5min, abandon"),
    ABANDONED(3,"Player dc, clicked leave, abandon"),
    AFK(4,"Player AFK, abandon"),
    NEVER_CONNECTED(5,"Never connected, no abandon"),
    NEVER_CONNECTED_TOO_LONG(6,"Too long to connect, no abandon");


    private final int key;
    private final String description;

    private LeaverStatus(int code, String desc){
        this.key = code;
        this.description = desc;
    }
    public int getKey(){return key;}
    public String getDescription(){
        return description;
    }

    public static LeaverStatus fromKey(int key) {
        for(LeaverStatus lType : LeaverStatus.values()) {
            if(lType.getKey() == key) {
                return lType;
            }
        }
        return null;
    }

}
