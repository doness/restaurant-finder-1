package com.example.laptop.finalproject.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dish_ {

    @SerializedName("dish_id")
    @Expose
    private Integer dishId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private String price;

    public Integer getDishId() {
        return dishId;
    }

    public void setDishId(Integer dishId) {
        this.dishId = dishId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
