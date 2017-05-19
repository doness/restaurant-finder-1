package com.example.laptop.finalproject.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptop.finalproject.R;
import com.example.laptop.finalproject.adapters.ReviewsAdapter;
import com.example.laptop.finalproject.constants.Constants;
import com.example.laptop.finalproject.contracts.FragmentsContract;
import com.example.laptop.finalproject.injection.MyApp;
import com.example.laptop.finalproject.models.DailyMenu;
import com.example.laptop.finalproject.models.Restaurant_;
import com.example.laptop.finalproject.models.UserReviewWrapper;
import com.example.laptop.finalproject.presenters.FragmentPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab2 extends Fragment implements FragmentsContract.ITabFragment {

    @Inject FragmentPresenter presenter;

    Unbinder unbinder;

    @BindView(R.id.srUserReviews) SwipeRefreshLayout srUserReviews;
    @BindView(R.id.rvUserReviews) RecyclerView rvUserReviews;
    @BindView(R.id.tvNoReviews) TextView tvNoReviews;

    Restaurant_ restaurant_data;
    List<UserReviewWrapper> userReviews_data;
    View view;
    ProgressDialog progressDialog;

    public Tab2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true); // save state on change orientation
        //inject presenter
        ((MyApp)getActivity().getApplication()).getRestaurants_component().inject(this);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //do presenter/butterknife bindings
        this.view = view;
        presenter.bind(this);
        unbinder = ButterKnife.bind(this, view);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(Constants.EN_PROGRESS_DIALOG);
        progressDialog.show();
        //set default value to the text view that is displayed if no user reviews are available
        tvNoReviews.setText("No Reviews Available");
        //initialise the Recycler View
        initialiseRecyclerView(view.getContext());
        //tell the presenter to fetch the user reviews for the given restaurant
        presenter.fetchUserReviews(Integer.parseInt(restaurant_data.getId()));
        //setup the swipe refresh layout
        setupRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //unbind butterknife and the presenter
        unbinder.unbind();
        presenter.unbind();
    }

    //method called in the presenter that returns the User reviews to the fragment
    //we store the data so that the recycler view can use it
    @Override
    public void receiveUserReviews(List<UserReviewWrapper> userReviews) {
        Log.i("User Reviews Received", "Size is: " + userReviews.size());
        this.userReviews_data = userReviews;
        if (userReviews.size() > 0) {
            //checks if there are any user reviews available and nullifies the No Reviews text view if reviews are available
            tvNoReviews.setText("");
        }
        //after succesfully getting the data, set the recycler view
        setupRecyclerView();

    }

    //method called in the PageAdapter that passes the data from the MainFragment to the tab
    @Override
    public void receiveRestaurantId(Restaurant_ restaurant) {
        Log.i("Debugging", "Inside Tab 2, name is: " + restaurant.getName());
        this.restaurant_data = restaurant;

    }

    //pass the relevant values to the recycler view
    private void setupRecyclerView() {
        rvUserReviews.setAdapter(new ReviewsAdapter(userReviews_data, R.layout.row_user_reviews,
                getActivity().getApplicationContext()));

        progressDialog.dismiss();
    }

    //setup the swipe refresh layout
    private void setupRefresh() {

        final Tab2 tab2 = this;

        srUserReviews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //make sure the tab is still active when the refresh finishes
                        if (tab2.isAdded()) {
                            presenter.fetchUserReviews(Integer.parseInt(restaurant_data.getId()));
                            srUserReviews.setRefreshing(false);
                        }
                    }
                }, 2500);
            }
        });
    }

    //assign a layout manager to the recycler view
    private void initialiseRecyclerView(Context context){
        rvUserReviews.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void getError(String error_message) {

        Toast.makeText(getContext(), error_message, Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
    }

    //unused method

    @Override
    public void receiveDailyMenu(List<DailyMenu> dailyMenus) {

    }
}
