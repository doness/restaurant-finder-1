package com.example.laptop.finalproject.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.laptop.finalproject.R;
import com.example.laptop.finalproject.models.DailyMenu;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DailyMenuAdapter extends RecyclerView.Adapter<DailyMenuAdapter.DailyMenuViewHolder> {

    private List<DailyMenu> dailyMenus;
    private int row_daily_menu;
    private Context applicationContext;

    public DailyMenuAdapter(List<DailyMenu> dailyMenu_data, int row_daily_menu, Context applicationContext) {

        this.dailyMenus = dailyMenu_data;
        this.row_daily_menu = row_daily_menu;
        this.applicationContext = applicationContext;
    }

    @Override
    public DailyMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(row_daily_menu, parent, false);

        return new DailyMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DailyMenuViewHolder holder, int position) {

        holder.tvDishName.setText(dailyMenus.get(0)
                .getDailyMenu().getDishes().get(position).getDish().getName());
        holder.tvDishPrice.setText(dailyMenus.get(0)
                .getDailyMenu().getDishes().get(position).getDish().getPrice());
    }

    @Override
    public int getItemCount() {

        return dailyMenus.get(0).getDailyMenu().getDishes().size();
    }

    public static class DailyMenuViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvDishName) TextView tvDishName;
        @BindView(R.id.tvDishPrice) TextView tvDishPrice;

        public DailyMenuViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setTag(itemView);
        }
    }
}
