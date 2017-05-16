package com.example.laptop.finalproject;

import com.example.laptop.finalproject.contracts.FragmentsContract;
import com.example.laptop.finalproject.interacters.MainInteracter;
import com.example.laptop.finalproject.models.DailyMenu;
import com.example.laptop.finalproject.models.DailyMenuResult;
import com.example.laptop.finalproject.models.ReviewsResult;
import com.example.laptop.finalproject.models.UserReviewWrapper;
import com.example.laptop.finalproject.presenters.FragmentPresenter;
import com.example.laptop.finalproject.services.IRestaurantsAPI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by laptop on 16/05/2017.
 */

public class FragmentPresenterTest {



    @Mock
    @Inject
    IRestaurantsAPI iRestaurantsAPI;

    @Mock
    FragmentsContract.ITabFragment iTabFragment1;

    @Mock
    FragmentsContract.ITabFragment iTabFragment2;

    @Mock
    MainInteracter interacter1;

    @Mock
    MainInteracter interacter2;

    @InjectMocks
    FragmentPresenter presenter1;

    @InjectMocks
    FragmentPresenter presenter2;

    @Mock
    ReviewsResult reviewsResult;

    @Mock
    UserReviewWrapper userReviewWrapper;

    @Mock
    List<UserReviewWrapper> userReviews;

    @Mock
    DailyMenuResult dailyMenuResult;

    @Mock
    DailyMenu dailyMenu;

    @Mock
    List<DailyMenu> dailyMenus;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        iTabFragment1 = mock(FragmentsContract.ITabFragment.class);
        iTabFragment2 = mock(FragmentsContract.ITabFragment.class);
        presenter1 = new FragmentPresenter(interacter1);
        presenter2 = new FragmentPresenter(interacter2);

        reviewsResult = new ReviewsResult();
        userReviewWrapper = new UserReviewWrapper();
        userReviews = new ArrayList<>();
        userReviews.add(userReviewWrapper);
        reviewsResult.setUserReviews(userReviews);

        dailyMenuResult = new DailyMenuResult();
        dailyMenu = new DailyMenu();
        dailyMenus = new ArrayList<>();
        dailyMenus.add(dailyMenu);
        dailyMenuResult.setDailyMenus(dailyMenus);

        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
    }

    @Test
    public void testFetchesUserReviews() throws Exception {

        when(interacter1.getUserReviewsUseCase(0))
                .thenReturn(Observable.just(reviewsResult));

        presenter1.bind(iTabFragment1);
        presenter1.fetchUserReviews(0);

        Mockito.verify(iTabFragment1).receiveUserReviews(userReviews);

        RxAndroidPlugins.getInstance().reset();
    }

    @Test
    public void testFetchesDailyMenu() throws Exception {

        when(interacter2.getDailyMenuUseCase(0))
                .thenReturn(Observable.just(dailyMenuResult));

        presenter2.bind(iTabFragment2);
        presenter2.fetchDailyMenu(0);

        Mockito.verify(iTabFragment2).receiveDailyMenu(dailyMenus);

        presenter2.unbind();
    }
}
