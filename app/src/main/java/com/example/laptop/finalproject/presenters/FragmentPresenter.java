package com.example.laptop.finalproject.presenters;

import com.example.laptop.finalproject.contracts.FragmentsContract;
import com.example.laptop.finalproject.interacters.MainInteracter;
import com.example.laptop.finalproject.models.DailyMenuResult;
import com.example.laptop.finalproject.models.ReviewsResult;
import com.example.laptop.finalproject.utilities.RxUtils;

import java.net.UnknownHostException;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class FragmentPresenter implements FragmentsContract.IFragmentPresenter{

    MainInteracter interacter;
    FragmentsContract.ITabFragment tabFragment;
    CompositeSubscription compositeSubscription;

    @Inject
    public FragmentPresenter (MainInteracter interacter) {

        this.interacter = interacter;
    }


    @Override
    public void bind(FragmentsContract.ITabFragment tabFragment) {
        this.tabFragment = tabFragment;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void fetchUserReviews(Integer res_id) {

        compositeSubscription.add(interacter.getUserReviewsUseCase(res_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReviewsResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        e.printStackTrace();

                        if(e.getClass() == UnknownHostException.class) {
                            tabFragment.getError("Error: No Internet Connection");
                        }

                        else {
                            tabFragment.getError(e.getMessage());
                        }

                    }

                    @Override
                    public void onNext(ReviewsResult userReviews) {

                        tabFragment.receiveUserReviews(userReviews.getUserReviews());
                    }
                }));
    }

    @Override
    public void fetchDailyMenu(Integer res_id) {

        compositeSubscription.add(interacter.getDailyMenuUseCase(res_id)
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
                }));
    }

    @Override
    public void unbind() {
        tabFragment = null;
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }
}
