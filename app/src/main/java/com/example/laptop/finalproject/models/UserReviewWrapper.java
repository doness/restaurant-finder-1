package com.example.laptop.finalproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserReviewWrapper {

    @SerializedName("review")
    @Expose
    private UserReview review;

    public UserReview getReview() {
        return review;
    }

    public void setReview(UserReview review) {
        this.review = review;
    }

}
