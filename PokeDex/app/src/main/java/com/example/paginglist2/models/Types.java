package com.example.paginglist2.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Types {

    @SerializedName("slot")
    @Expose
    private Integer slot;
    @SerializedName("type")
    @Expose
    private Type type;

    public Integer getSlot() {
        return slot;
    }

    public Type getType() {
        return type;
    }

}