package com.example.laptop.finalproject;


import android.content.Context;

import com.example.laptop.finalproject.constants.Constants;
import com.example.laptop.finalproject.contracts.MainContract;
import com.example.laptop.finalproject.interacters.MainInteracter;
import com.example.laptop.finalproject.models.Location;
import com.example.laptop.finalproject.models.MarkerData;
import com.example.laptop.finalproject.models.MarkerDataParcel;
import com.example.laptop.finalproject.models.Restaurant;
import com.example.laptop.finalproject.models.Restaurant_;
import com.example.laptop.finalproject.models.Results;
import com.example.laptop.finalproject.models.UserRating;
import com.example.laptop.finalproject.presenters.MainPresenter;
import com.example.laptop.finalproject.services.IRestaurantsAPI;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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

public class MainPresenterTest {

    @Mock
    @Inject
    IRestaurantsAPI iRestaurantsAPI;

    @Mock
    MainContract.IMainView iMainView;

    @Mock
    MainInteracter interacter;

    @InjectMocks
    MainPresenter presenter;

    @Mock
    Context context;

    @Mock
    Results results;

    @Mock
    Restaurant restaurant;

    @Mock
    Restaurant_ restaurant_;

    @Mock
    Location location;

    @Mock
    UserRating userRating;

    @Mock
    MarkerDataParcel markerDataParcel;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        iMainView = mock(MainContract.IMainView.class);

        presenter = new MainPresenter(interacter);
        location = new Location();
        userRating = new UserRating();
        restaurant_ = new Restaurant_();
        restaurant = new Restaurant();
        results = new Results();

        location.setLatitude("12.0");
        location.setLongitude("12.0");

        userRating.setAggregateRating(4.5);

        restaurant_.setCuisines("Fast Food");
        restaurant_.setAverageCostForTwo(2);
        restaurant_.setId("id_data");
        restaurant_.setLocation(location);
        restaurant_.setName("restaurant name");
        restaurant_.setUserRating(userRating);

        restaurant.setRestaurant(restaurant_);

        List<Restaurant> restaurants = new ArrayList<>();

        restaurants.add(restaurant);

        results.setRestaurants(restaurants);

        MarkerData markerData = new MarkerData("id_data", 12.0, 12.0, "restaurant name", 2, 4.5, "Fast Food");

        List<MarkerData> markerDataList = new ArrayList<>();
        markerDataList.add(markerData);

        markerDataParcel = new MarkerDataParcel(markerDataList);

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

    //test if user input verification works as intended
    @Test
    public void testPresenterUserInputVerificationCorrect() {

        presenter.bind(iMainView);

        presenter.getUserInputs(context, Constants.USE_MY_LOCATION, "", "", "", "");

        Mockito.verify(iMainView).confirmData(true);
    }

    //test if the verification catches invalid user input
    @Test
    public void testPresenterUserInputVerificationIncorrect() {

        presenter.bind(iMainView);

        presenter.getUserInputs(context, "", "", "", "", "");

        Mockito.verify(iMainView).confirmData(false);

        presenter.unbind();
    }

    @Ignore
    @Test
    public void testPresenterFetchesData() {

        when(interacter.getResultsUseCase(0, 12, 12, "Fast Food", "Delivery"))
                .thenReturn(Observable.just(results));

        presenter.bind(iMainView);
        presenter.fetchMarkerData();

        Mockito.verify(iMainView).startMapActivity(markerDataParcel);
    }



}
