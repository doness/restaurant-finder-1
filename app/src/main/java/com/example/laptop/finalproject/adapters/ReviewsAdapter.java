package com.example.laptop.finalproject.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.laptop.finalproject.R;
import com.example.laptop.finalproject.models.User;
import com.example.laptop.finalproject.models.UserReviewWrapper;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by laptop on 15/05/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder>{

    List<UserReviewWrapper> userReviews;
    int row_user_reviews;
    Context applicationContext;

    public ReviewsAdapter(List<UserReviewWrapper> userReviews_data, int row_user_reviews, Context applicationContext) {
        this.userReviews = userReviews_data;
        this.row_user_reviews = row_user_reviews;
        this.applicationContext = applicationContext;
    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(row_user_reviews,
                parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        Log.i("Debugging", "Inside Adapter, user review size: " + userReviews.size());
        Log.i("Debugging", "Position is " + position);

        User user = userReviews.get(position).getReview().getUser();
        Log.i("Debugging", "Url is " + user.getProfileImage());
        Picasso.with(applicationContext).load(user.getProfileImage())
                .into(holder.ivUserAvatar);
        holder.tvUserName.setText(user.getName());
        holder.tvUserLevel.setText(user.getFoodieLevel());
        holder.tvReviewDate.setText("Reviewed: " + userReviews.get(position).getReview().getReviewTimeFriendly());
        String rating_end = " ";
        if ((userReviews.get(position).getReview().getRating()).length() == 1){
            rating_end = ".0 ";
        }
        holder.tvUserRatingNumber.setText(" " + userReviews.get(position).getReview().getRating() + rating_end);
        holder.tvUserRatingNumber.setBackgroundColor(Color.
                parseColor("#" + userReviews.get(position).getReview().getRatingColor()));
        holder.tvUserRatingTitle.setText(" " + userReviews.get(position).getReview().getRatingText());
        holder.tvReviewBody.setText(userReviews.get(position).getReview().getReviewText());
        Picasso.with(applicationContext).load("http://www.freeiconspng.com/uploads/youtube-like-png-28.png")
                .into(holder.ivLikes);
        holder.tvLikes.setText(" Likes: " + userReviews.get(position).getReview().getLikes());
        holder.tvUserLevelNum.setText("Level: " + user.getFoodieLevelNum());
    }

    @Override
    public int getItemCount() {
        return userReviews.size();
    }

    public static class ReviewsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ivUserAvatar) ImageView ivUserAvatar;
        @BindView(R.id.tvUserName) TextView tvUserName;
        @BindView(R.id.tvUserLevel) TextView tvUserLevel;
        @BindView(R.id.tvReviewDate) TextView tvReviewDate;
        @BindView(R.id.tvUserRatingNumber) TextView tvUserRatingNumber;
        @BindView(R.id.tvUserRatingTitle) TextView tvUserRatingTitle;
        @BindView(R.id.tvReviewBody) TextView tvReviewBody;
        @BindView(R.id.ivLikes) ImageView ivLikes;
        @BindView(R.id.tvLikes) TextView tvLikes;
        @BindView(R.id.tvUserLevelNum) TextView tvUserLevelNum;


        public ReviewsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setTag(itemView);
        }
    }
}
