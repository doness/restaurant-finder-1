package com.example.laptop.finalproject.contracts;


import android.content.Context;

public interface MainContract {

    interface IMainPresenter{

        void bind(IMainView view);
        void getUserInputs(Context context, String location, String cuisine, String category, String price, String reviews);
        void unbind();
    }

    interface IMainView{

        void confirmData(boolean dataState);
    }
}

