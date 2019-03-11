package com.hamaksoftware.mydota.activeandroid.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hamaksoftware.mydota.enums.HeroType;

@Table(name = "Heroes")
public class Hero extends Model {

    @Expose
    @Column(name = "HeroId")
    public int heroId;

    @Expose
    @Column(name = "Name")
    public String name;

    @Expose
    @Column(name = "LocalizedName")
    public String localizedName;

    @Expose
    @Column(name = "HeroType")
    @SerializedName("type")
    public HeroType type;

    public Hero() {
        super();
    }
}

