package com.example.laptop.finalproject.models;


import android.os.Parcel;
import android.os.Parcelable;


public class MarkerData implements Parcelable {

    public String restaurant_id;
    public double restaurant_lat;
    public double restaurant_lon;
    public String restaurant_name;
    public Integer restaurant_price;
    public double restaurant_rating;
    public String restaurant_cuisines;
    public Integer location_check;

    public MarkerData(String restaurant_id, double restaurant_lat, double restaurant_lon,
                      String restaurant_name, Integer restaurant_price, double restaurant_rating,
                      String restaurant_cuisines, Integer location_check) {
        this.restaurant_id = restaurant_id;
        this.restaurant_lat = restaurant_lat;
        this.restaurant_lon = restaurant_lon;
        this.restaurant_name = restaurant_name;
        this.restaurant_price = restaurant_price;
        this.restaurant_rating = restaurant_rating;
        this.restaurant_cuisines = restaurant_cuisines;
        this.location_check = location_check;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.restaurant_id);
        dest.writeDouble(this.restaurant_lat);
        dest.writeDouble(this.restaurant_lon);
        dest.writeString(this.restaurant_name);
        dest.writeInt(this.restaurant_price);
        dest.writeDouble(this.restaurant_rating);
        dest.writeString(this.restaurant_cuisines);
        dest.writeInt(this.location_check);

    }

    protected MarkerData (Parcel in) {
        this.restaurant_id = in.readString();
        this.restaurant_lat = in.readDouble();
        this.restaurant_lon = in.readDouble();
        this.restaurant_name = in.readString();
        this.restaurant_price = in.readInt();
        this.restaurant_rating = in.readDouble();
        this.restaurant_cuisines = in.readString();
        this.location_check = in.readInt();

    }

    public static final Creator<MarkerData> CREATOR = new Creator<MarkerData>() {
        public MarkerData createFromParcel(Parcel source) {
            return new MarkerData(source);
        }

        public MarkerData[] newArray(int size) {
            return new MarkerData[size];
        }
    };
}
