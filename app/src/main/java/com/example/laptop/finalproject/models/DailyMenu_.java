package com.example.laptop.finalproject.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DailyMenu_ {

    @SerializedName("daily_menu_id")
    @Expose
    private Integer dailyMenuId;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("dishes")
    @Expose
    private List<Dish> dishes = null;

    public Integer getDailyMenuId() {
        return dailyMenuId;
    }

    public void setDailyMenuId(Integer dailyMenuId) {
        this.dailyMenuId = dailyMenuId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

}
