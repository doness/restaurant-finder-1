package com.example.laptop.finalproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DailyMenu {

    @SerializedName("daily_menu")
    @Expose
    private DailyMenu_ dailyMenu;

    public DailyMenu_ getDailyMenu() {
        return dailyMenu;
    }

    public void setDailyMenu(DailyMenu_ dailyMenu) {
        this.dailyMenu = dailyMenu;
    }

}
