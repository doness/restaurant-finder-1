package com.example.laptop.finalproject.services;

import com.example.laptop.finalproject.constants.Constants;
import com.example.laptop.finalproject.injection.MyApp;
import com.example.laptop.finalproject.interacters.MainInteracter;
import com.example.laptop.finalproject.models.DailyMenuResult;
import com.example.laptop.finalproject.models.Restaurant_;
import com.example.laptop.finalproject.models.Results;
import com.example.laptop.finalproject.models.ReviewsResult;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class RestaurantsService implements MainInteracter {


    public RestaurantsService() {
        getConnection();
    }

    public static IRestaurantsAPI getConnection(){

        Retrofit retrofit = null;
        OkHttpClient okHttpClient = null;

        /**
         * Used to print the log statements of the parsed json data in the logcat
         */

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        /**
         * Add HttpLoginInterecptor to okhttp
         */
        Cache cache = new Cache(MyApp.getContext().getCacheDir(), 10 * 1024 * 1024);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .cache(cache)
                .build();


        if (retrofit==null){
            retrofit= new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    /**
                     * used to parse json to pojos
                     */
                    .addConverterFactory(GsonConverterFactory.create())
                    /**
                     * Display data received to RecyclerView
                     */
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    /**
                     * Add Okthhp as a friend
                     */
                    .client(okHttpClient)
                    .build();
        }

        return retrofit.create(IRestaurantsAPI.class);
    }

    @Override
    public Observable<Results> getResultsUseCase(int start, double lat, double lon,
                                                 String cuisines, String category) {
        return getConnection().getSearchResults(start, lat, lon, 2500, cuisines, category);
    }

    @Override
    public Observable<Restaurant_> getRestaurantUseCase(Integer res_id) {
        return getConnection().getRestaurant(res_id);
    }

    @Override
    public Observable<ReviewsResult> getUserReviewsUseCase(Integer res_id) {
        return getConnection().getUserReviews(res_id);
    }

    @Override
    public Observable<DailyMenuResult> getDailyMenuUseCase(Integer res_id) {
        return getConnection().getDailyMenu(res_id);
    }

}