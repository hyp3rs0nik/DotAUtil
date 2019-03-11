package com.hamaksoftware.mydota.enums;
public enum CommunityVisibilityState {
    PRIVATE(1),FRIENDS_ONLY(2),FRIENDS_OF_FRIENDS(3),USERS_ONLY(4),PUBLIC(5);

    private final int key;
    private CommunityVisibilityState(int code){
        this.key = code;
    }
    public int getKey(){return key;}
    public static CommunityVisibilityState fromKey(int key) {
        for(CommunityVisibilityState type : CommunityVisibilityState.values()) {
            if(type.getKey() == key) {
                return type;
            }
        }
        return null;
    }

}
