package com.example.laptop.finalproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRating {

    @SerializedName("aggregate_rating")
    @Expose
    private double aggregateRating;
    @SerializedName("rating_text")
    @Expose
    private String ratingText;
    @SerializedName("rating_color")
    @Expose
    private String ratingColor;
    @SerializedName("votes")
    @Expose
    private Integer votes;

    public double getAggregateRating() {
        return aggregateRating;
    }

    public void setAggregateRating(double aggregateRating) {
        this.aggregateRating = aggregateRating;
    }

    public String getRatingText() {
        return ratingText;
    }

    public void setRatingText(String ratingText) {
        this.ratingText = ratingText;
    }

    public String getRatingColor() {
        return ratingColor;
    }

    public void setRatingColor(String ratingColor) {
        this.ratingColor = ratingColor;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

}
