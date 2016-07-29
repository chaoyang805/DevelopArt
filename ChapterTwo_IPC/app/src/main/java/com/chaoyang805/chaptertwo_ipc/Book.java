package com.chaoyang805.chaptertwo_ipc;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chaoyang805 on 16/7/25.
 */

public class Book implements Parcelable {
    public int bookId;
    public String bookName;

    public Book(int bookId, String bookName) {
        this.bookId = bookId;
        this.bookName = bookName;
    }

    protected Book(Parcel in) {
        bookId = in.readInt();
        bookName = in.readString();
    }


    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flag) {
        out.writeInt(bookId);
        out.writeString(bookName);
    }

    @Override
    public String toString() {
        return bookId + " " + bookName;
    }
}
