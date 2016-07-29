package com.chaoyang805.chaptertwo_ipc;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by chaoyang805 on 16/7/25.
 */

public class User implements Serializable, Parcelable {
    private static final long serialVersionUID = 29233423354343L;
    public int userId;
    public String userName;
    public boolean isMale;

    public User() {

    }

    public User(int userId, String userName, boolean isMale) {
        this.userId = userId;
        this.userName = userName;
        this.isMale = isMale;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flag) {
        out.writeInt(userId);
        out.writeString(userName);
        out.writeInt(isMale ? 1 : 0);
    }

    public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel parcel) {

            return new User(parcel);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {
        userId = in.readInt();
        userName = in.readString();
        isMale = in.readInt() == 1;
    }

    @Override
    public String toString() {
        return userId + " " + userName;

    }
}
