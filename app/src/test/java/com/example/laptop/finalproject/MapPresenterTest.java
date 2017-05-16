package com.example.laptop.finalproject;

import android.content.Context;

import com.example.laptop.finalproject.contracts.MainContract;
import com.example.laptop.finalproject.interacters.MainInteracter;
import com.example.laptop.finalproject.models.Location;
import com.example.laptop.finalproject.models.Restaurant_;
import com.example.laptop.finalproject.models.UserRating;
import com.example.laptop.finalproject.presenters.MapPresenter;
import com.example.laptop.finalproject.services.IRestaurantsAPI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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

public class MapPresenterTest {

    @Mock
    @Inject
    IRestaurantsAPI iRestaurantsAPI;

    @Mock
    MainContract.IMapView iMapView;

    @Mock
    MainInteracter interacter;

    @InjectMocks
    MapPresenter presenter;

    @Mock
    Context context;

    @Mock
    Restaurant_ restaurant_;

    @Mock
    Location location;

    @Mock
    UserRating userRating;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        iMapView = mock(MainContract.IMapView.class);
        presenter = new MapPresenter(interacter);
        location = new Location();
        userRating = new UserRating();
        restaurant_ = new Restaurant_();

        location.setLatitude("12.0");
        location.setLongitude("12.0");

        userRating.setAggregateRating(4.5);

        restaurant_.setCuisines("Fast Food");
        restaurant_.setAverageCostForTwo(2);
        restaurant_.setId("id_data");
        restaurant_.setLocation(location);
        restaurant_.setName("restaurant name");
        restaurant_.setUserRating(userRating);

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
    public void testFetchesDataCorrectly() throws Exception {

        when(interacter.getRestaurantUseCase(0))
                .thenReturn(Observable.just(restaurant_));

        presenter.bind(iMapView);
        presenter.fetchRestaurant(0);

        Mockito.verify(iMapView).getRestaurantData(restaurant_);

        presenter.unbind();
    }
}