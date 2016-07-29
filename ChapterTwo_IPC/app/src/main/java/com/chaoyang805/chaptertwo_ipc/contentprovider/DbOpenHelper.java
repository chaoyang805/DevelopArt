package com.chaoyang805.chaptertwo_ipc.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chaoyang805 on 16/7/29.
 */

public class DbOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "book.db";

    private static final int DB_VERSION = 1;

    public static final String BOOK_TABLE_NAME = "book";

    public static final String USER_TABLE_NAME = "user";

    private static final String CREATE_TABLE_BOOK =
        "CREATE TABLE IF NOT EXISTS " +
        BOOK_TABLE_NAME +
        " ( _id INTEGER PRIMARY KEY," +
        " name TEXT)";

    private static final String CREATE_TABLE_USER =
        "CREATE TABLE IF NOT EXISTS " +
        USER_TABLE_NAME +
        " ( _id INTEGER PRIMARY KEY, " +
        " name TEXT, " +
        " sex INT)";

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_BOOK);
        database.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
