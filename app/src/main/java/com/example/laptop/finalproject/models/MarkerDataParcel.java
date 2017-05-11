package com.example.laptop.finalproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class MarkerDataParcel implements Parcelable{

    public List<MarkerData> markerDataList;

    public MarkerDataParcel(List<MarkerData> markerDataList) {

        this.markerDataList = markerDataList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeList(markerDataList);
    }

    protected MarkerDataParcel (Parcel in) {

        markerDataList = new ArrayList<>();
        in.readList(markerDataList, MarkerData.class.getClassLoader());
    }

    public static final Creator<MarkerDataParcel> CREATOR = new Creator<MarkerDataParcel>() {
        public MarkerDataParcel createFromParcel(Parcel source) {
            return new MarkerDataParcel(source);
        }

        public MarkerDataParcel[] newArray(int size) {
            return new MarkerDataParcel[size];
        }
    };
}
