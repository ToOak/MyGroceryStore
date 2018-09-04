package com.example.xushuailong.mygrocerystore.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class PathPosition implements Parcelable {
    public float firstX;
    public float firstY;
    public float nextX;
    public float nextY;

    public PathPosition() {
    }

    protected PathPosition(Parcel in) {
        firstX = in.readFloat();
        firstY = in.readFloat();
        nextX = in.readFloat();
        nextY = in.readFloat();
    }

    public static final Creator<PathPosition> CREATOR = new Creator<PathPosition>() {
        @Override
        public PathPosition createFromParcel(Parcel in) {
            return new PathPosition(in);
        }

        @Override
        public PathPosition[] newArray(int size) {
            return new PathPosition[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(firstX);
        dest.writeFloat(firstY);
        dest.writeFloat(nextX);
        dest.writeFloat(nextY);
    }
}
