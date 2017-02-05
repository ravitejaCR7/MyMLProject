package com.example.talarir.mymlproject.TheProject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by talarir on 04/02/2017.
 */

public class ParcelableMajor implements Parcelable{

    String latitude;
    String longitude;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(latitude);
        dest.writeString(longitude);
    }

    public ParcelableMajor(String latitude,String longitude)
    {
        this.latitude=latitude;
        this.longitude=longitude;
    }

    private ParcelableMajor(Parcel in)
    {
        this.latitude=in.readString();
        this.longitude=in.readString();
    }

    public static final Creator<ParcelableMajor> CREATOR = new Creator<ParcelableMajor>() {
        @Override
        public ParcelableMajor createFromParcel(Parcel in) {
            return new ParcelableMajor(in);
        }

        @Override
        public ParcelableMajor[] newArray(int size) {
            return new ParcelableMajor[size];
        }
    };

}
