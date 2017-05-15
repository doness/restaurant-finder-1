package com.example.laptop.finalproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dish {

    @SerializedName("dish")
    @Expose
    private Dish_ dish;

    public Dish_ getDish() {
        return dish;
    }

    public void setDish(Dish_ dish) {
        this.dish = dish;
    }

}
