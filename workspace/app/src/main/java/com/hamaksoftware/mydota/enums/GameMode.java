package com.hamaksoftware.mydota.enums;

public enum GameMode {
    ALL_PICK(1, "All Pick", "All Pick"),
    CAPTAINS_MODE(2, "", ""),
    RANDOM_DRAFT(3, "", ""),
    SINGLE_DRAFT(4, "", ""),
    ALL_RANDOM(5, "", ""),
    INTRO_DEATH(6, "", ""),
    DIRETIDE(7, "", ""),
    REVERSE_CAPTAINS_MODE(8, "", ""),
    GREEVILING(9, "", ""),
    TUTORIAL(10, "", ""),
    MID_ONLY(11, "", ""),
    LEAST_PLAYED(12, "", ""),
    NEW_PLAYER_POOL(13, "", "");

    public int code;
    public String label;
    public String description;

    private GameMode(int c, String label, String description) {
        this.code = c;
        this.label = label;
        this.description = description;
    }
}
