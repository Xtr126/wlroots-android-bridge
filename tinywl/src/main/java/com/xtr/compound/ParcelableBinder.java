package com.xtr.compound;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableBinder implements Parcelable {

    private final IBinder mBinder;

    public ParcelableBinder(IBinder binder) {
        mBinder = binder;
    }

    private ParcelableBinder(Parcel in) {
        mBinder = in.readStrongBinder();
    }

    public IBinder getBinder() {
        return mBinder;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStrongBinder(mBinder);
    }

    public static final Creator<ParcelableBinder> CREATOR = new Creator<ParcelableBinder>() {
        @Override
        public ParcelableBinder createFromParcel(Parcel in) {
            return new ParcelableBinder(in);
        }

        @Override
        public ParcelableBinder[] newArray(int size) {
            return new ParcelableBinder[size];
        }
    };
}
