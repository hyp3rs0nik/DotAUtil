package com.hamaksoftware.mydota.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hamaksoftware.mydota.activeandroid.model.Hero;
import com.hamaksoftware.mydota.activeandroid.model.Item;
import com.hamaksoftware.mydota.enums.LeaverStatus;

import java.util.List;

public class Player {
    @Expose
    public User user;

    @Expose
    public int slot;

    @Expose
    @SerializedName("heroId")
    public Hero hero;

    @Expose
    List<Item> items;

    @Expose
    public int kills;

    @Expose
    public int deaths;

    @Expose
    public int assists;

    @Expose
    public LeaverStatus leaverStatus;

    @Expose
    public int gold;

    @Expose
    public int lastHits;

    @Expose
    public int denies;

    @Expose
    public int goldPerMin;

    @Expose
    public int xpPerMin;

    @Expose
    public int goldSpent;

    @Expose
    public int heroDamage;

    @Expose
    public int towerDamage;

    @Expose
    public int heroHealing;

    @Expose
    public int level;

    @Expose
    public List<AbilityUpgrade> abilityUpgrades;


    public Player() {}
}
