package com.example.laptop.finalproject.contracts;


public interface MainContract {

    interface IMainPresenter{

        void getUserInputs(String location, String cuisine, String category, String price, String reviews);
    }

    interface IMainView{

        void confirmData(String dataState);
    }
}

