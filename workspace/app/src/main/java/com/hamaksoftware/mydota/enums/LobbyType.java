package com.hamaksoftware.mydota.enums;
public enum LobbyType {
    INVALID(-1,"Invalid"),PUBLIC_MATCHMAKING(0,"Public Matchmaking"),PRACTICE(1,"Practice"),TOURNAMENT(2,"Tournament"),
    TUTORIAL(3,"Tutorial"),TEAM_MATCH(5,"Team match"),COOP_WITH_BOTS(4,"Co-op with bots"),SOLO_QUEUE(6, "Solo Queue"),
    RANKED(7, "Ranked"),SOLO_MID_LEVEL(8, "Solo Mid 1vs1");


    private final int key;
    private final String description;

    private LobbyType(int code, String desc){
        this.key = code;
        this.description = desc;
    }
    public int getKey(){return key;}
    public String getDescription(){
        return description;
    }

    public static LobbyType fromKey(int key) {
        for(LobbyType lType : LobbyType.values()) {
            if(lType.getKey() == key) {
                return lType;
            }
        }
        return null;
    }

}
