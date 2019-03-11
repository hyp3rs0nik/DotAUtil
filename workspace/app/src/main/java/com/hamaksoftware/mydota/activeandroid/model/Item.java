package com.hamaksoftware.mydota.activeandroid.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.hamaksoftware.mydota.enums.HeroType;

@Table(name = "Items")
public class Item extends Model {

    @Expose
    @Column(name = "itemId")
    public int id;

    @Expose
    @Column(name = "Name")
    public String name;

    @Expose
    @Column(name = "LocalizedName")
    public String localizedName;


    public Item() {
        super();
    }
}

