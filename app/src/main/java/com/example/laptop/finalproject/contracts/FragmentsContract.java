package com.example.laptop.finalproject.contracts;

import com.example.laptop.finalproject.models.Restaurant_;
import com.example.laptop.finalproject.models.UserReviewWrapper;

import java.util.List;


public interface FragmentsContract {

    interface IFragmentPresenter{

        void bind(ITabFragment tabFragment);
        void fetchUserReviews(Integer res_id);
        void unbind();
    }

    interface IMainFragment{

        void receiveRestaurantData(Restaurant_ restaurant);
    }

    interface ITabFragment{

        void receiveRestaurantId(Restaurant_ restaurant);
        void receiveUserReviews(List<UserReviewWrapper> userReviews);
    }
}
