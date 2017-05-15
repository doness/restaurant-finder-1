package com.example.laptop.finalproject.presenters;

import com.example.laptop.finalproject.contracts.FragmentsContract;
import com.example.laptop.finalproject.interacters.MainInteracter;
import com.example.laptop.finalproject.models.DailyMenuResult;
import com.example.laptop.finalproject.models.ReviewsResult;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FragmentPresenter implements FragmentsContract.IFragmentPresenter{

    MainInteracter interacter;
    FragmentsContract.ITabFragment tabFragment;

    @Inject
    public FragmentPresenter (MainInteracter interacter) {

        this.interacter = interacter;
    }


    @Override
    public void bind(FragmentsContract.ITabFragment tabFragment) {
        this.tabFragment = tabFragment;
    }

    @Override
    public void fetchUserReviews(Integer res_id) {

        interacter.getUserReviewsUseCase(res_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReviewsResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(ReviewsResult userReviews) {

                        tabFragment.receiveUserReviews(userReviews.getUserReviews());
                    }
                });
    }

    @Override
    public void fetchDailyMenu(Integer res_id) {

        interacter.getDailyMenuUseCase(res_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DailyMenuResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        e.printStackTrace();
                        fetchDailyMenu(16507624);

                    }

                    @Override
                    public void onNext(DailyMenuResult dailyMenus) {

                        tabFragment.receiveDailyMenu(dailyMenus.getDailyMenus());
                    }
                });
    }

    @Override
    public void unbind() {
        tabFragment = null;
    }
}
