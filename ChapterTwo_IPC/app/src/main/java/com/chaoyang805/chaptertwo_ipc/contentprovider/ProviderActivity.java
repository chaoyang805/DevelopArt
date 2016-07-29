package com.chaoyang805.chaptertwo_ipc.contentprovider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.chaoyang805.chaptertwo_ipc.Book;
import com.chaoyang805.chaptertwo_ipc.Debugable;
import com.chaoyang805.chaptertwo_ipc.R;
import com.chaoyang805.chaptertwo_ipc.User;

/**
 * Created by chaoyang805 on 16/7/29.
 */

public class ProviderActivity extends AppCompatActivity implements Debugable {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        Uri  bookUri = Uri.parse("content://com.chaoyang805.chaptertwo_ipc.contentprovider.BookProvider/book");
        ContentValues cv = new ContentValues();
        cv.put("_id", 6);
        cv.put("name", "程序设计的艺术");

        getContentResolver().insert(bookUri, cv);
        Cursor bookCursor = getContentResolver().query(bookUri, new String[]{"_id", "name"}, null, null, null);
        while (bookCursor.moveToNext()) {
            int id = bookCursor.getInt(bookCursor.getColumnIndex("_id"));
            String name = bookCursor.getString(bookCursor.getColumnIndex("name"));
            Book book = new Book(id, name);
            localLog("query book: " + book.toString());
        }
        bookCursor.close();

        Uri userUri = Uri.parse("content://com.chaoyang805.chaptertwo_ipc.contentprovider.BookProvider/user");
        Cursor userCursor = getContentResolver().query(userUri, new String[]{"_id", "name", "sex"}, null, null, null);
        while (userCursor.moveToNext()) {
            int id = userCursor.getInt(userCursor.getColumnIndex("_id"));
            String name = userCursor.getString(userCursor.getColumnIndex("name"));
            boolean isMale = userCursor.getInt(userCursor.getColumnIndex("sex")) == 1;
            User user = new User(id, name, isMale);
            localLog("query user: " + user.toString());
        }
        userCursor.close();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void localLog(String msg) {
        final String TAG = "ProviderActivity";
        Log.d(TAG, msg);
    }
}
