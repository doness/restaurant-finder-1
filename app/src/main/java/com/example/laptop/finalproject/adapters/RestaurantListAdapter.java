package com.example.laptop.finalproject.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.laptop.finalproject.MapsActivity;
import com.example.laptop.finalproject.R;
import com.example.laptop.finalproject.constants.Constants;
import com.example.laptop.finalproject.models.MarkerData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder> {

    List<MarkerData> listData;
    int row_restaurant_list;
    Context applicationContext;
    MapsActivity parentActivity;

    public RestaurantListAdapter(MapsActivity parentActivity, List<MarkerData> listData, int row_restaurant_list, Context applicationContext) {

        this.listData = listData;
        this.row_restaurant_list = row_restaurant_list;
        this. applicationContext = applicationContext;
        this.parentActivity = parentActivity;
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(row_restaurant_list, parent, false);

        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, final int position) {


        int temp_position = listData.get(position).restaurant_price;
        String temp_price = Constants.EN_PRICE_LIST[temp_position];

        holder.tvListRestaurantName.setText(listData.get(position).restaurant_name);
        holder.tvListCuisines.setText(listData.get(position).restaurant_cuisines);
        holder.tvListPrice.setText(temp_price);
        holder.rbListRating.setRating((float)(listData.get(position).restaurant_rating));
        holder.btnListShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                parentActivity.showMarkerFromList(position);
            }
        });
        holder.btnListViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                parentActivity.getDataFromList(Integer.parseInt(listData.get(position).restaurant_id));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvListRestaurantName) TextView tvListRestaurantName;
        @BindView(R.id.tvListCuisines) TextView tvListCuisines;
        @BindView(R.id.tvListPrice) TextView tvListPrice;
        @BindView(R.id.rbListRating) RatingBar rbListRating;
        @BindView(R.id.btnListShowOnMap) Button btnListShowOnMap;
        @BindView(R.id.btnListViewDetails) Button btnListViewDetails;

        public RestaurantViewHolder (View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setTag(itemView);
        }
    }
}
