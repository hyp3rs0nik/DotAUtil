package com.hamaksoftware.mydota.model;

import com.google.gson.annotations.Expose;
import com.hamaksoftware.mydota.enums.LobbyType;

import java.util.List;
public class Match {

    @Expose
    public long id;

    @Expose
    public long seqNum;

    @Expose
    public long startTime;

    @Expose
    public LobbyType lobbyType;

    @Expose
    public List<Player> players;

    @Expose
    public boolean radiantWin;

    public Match() {}
}
