package com.example.laptop.finalproject.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by laptop on 05/06/2017.
 */

public class MAStateParcel implements Parcelable{

    public Integer language;
    public Integer location_check;
    public String location_text;

    public MAStateParcel(Integer language, Integer location_check, String location_text) {

        this.language = language;
        this.location_check = location_check;
        this. location_text = location_text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(this.language);
        dest.writeInt(this.location_check);
        dest.writeString(this.location_text);
    }

    protected MAStateParcel (Parcel in) {

        this.language = in.readInt();
        this.location_check = in.readInt();
        this.location_text = in.readString();
    }

    public static final Creator<MAStateParcel> CREATOR = new Creator<MAStateParcel>() {
        public MAStateParcel createFromParcel(Parcel source) {
            return new MAStateParcel(source);
        }

        public MAStateParcel[] newArray(int size) {
            return new MAStateParcel[size];
        }
    };
}
