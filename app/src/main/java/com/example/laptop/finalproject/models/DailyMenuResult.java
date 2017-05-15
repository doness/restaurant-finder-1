package com.example.laptop.finalproject.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DailyMenuResult {

    @SerializedName("daily_menus")
    @Expose
    private List<DailyMenu> dailyMenus = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<DailyMenu> getDailyMenus() {
        return dailyMenus;
    }

    public void setDailyMenus(List<DailyMenu> dailyMenus) {
        this.dailyMenus = dailyMenus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
