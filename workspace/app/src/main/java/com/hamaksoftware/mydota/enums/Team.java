package com.hamaksoftware.mydota.enums;

public enum Team {

    RADIANT(0, "Radiant", "Radiant Team"),
    DIRE(1, "Dire", "Dire Team"),
    BROADCASTER(2, "Broadcaster", "Broadcaster"),
    UNASSIGNED(4, "Unassigned", "No Team");

    public int code;
    public String label;
    public String description;

    private Team(int c, String label, String description) {
        this.code = c;
        this.label = label;
        this.description = description;
    }

}
