package com.chaoyang805.chaptertwo_ipc.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.chaoyang805.chaptertwo_ipc.Debugable;

/**
 * Created by chaoyang805 on 16/7/29.
 */

public class BookProvider extends ContentProvider implements Debugable {

    private static final String TAG = "BookProvider";
    public static final String AUTHORITY = "com.chaoyang805.chaptertwo_ipc.contentprovider.BookProvider";
    public static final int BOOK_URI_CODE = 1;
    public static final int USER_URI_CODE = 2;
    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE);
        sUriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);
    }

    private SQLiteDatabase mDatabase;
    private Context mContext;
    @Override
    public boolean onCreate() {
        localLog("onCreate current thread " + Thread.currentThread().getName());

        mContext = getContext();
        initData();
        return true;
    }

    private void initData() {
        mDatabase = new DbOpenHelper(mContext).getWritableDatabase();
        mDatabase.execSQL("delete from " + DbOpenHelper.BOOK_TABLE_NAME);
        mDatabase.execSQL("delete from " + DbOpenHelper.USER_TABLE_NAME);
        mDatabase.execSQL("insert into " + DbOpenHelper.BOOK_TABLE_NAME + " values (3, 'Android');");
        mDatabase.execSQL("insert into " + DbOpenHelper.BOOK_TABLE_NAME + " values (4, 'iOS');");
        mDatabase.execSQL("insert into " + DbOpenHelper.BOOK_TABLE_NAME + " values (5, 'HTML5');");

        mDatabase.execSQL("insert into " + DbOpenHelper.USER_TABLE_NAME + " values (1, 'jake', 1);");
        mDatabase.execSQL("insert into " + DbOpenHelper.USER_TABLE_NAME + " values (2, 'jasmine', 0);");
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        localLog("query");
        String tableName = getTableName(uri);
        return mDatabase.query(tableName, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        localLog("insert");
        String tableName = getTableName(uri);
        long rowId = mDatabase.insert(tableName, null, contentValues);
        if (rowId != -1) {
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return uri;
    }

    @Override
    public int delete(Uri uri, String whereClause, String[] whereArgs) {
        localLog("delete");
        String tableName = getTableName(uri);
        int rows = mDatabase.delete(tableName, whereClause, whereArgs);
        if (rows > 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String whereClause, String[] whereArgs) {
        localLog("update");
        String tableName = getTableName(uri);
        int rows = mDatabase.update(tableName, contentValues, whereClause, whereArgs);
        if (rows > 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return rows;
    }

    @NonNull
    public String getTableName(Uri uri) {
        int code = sUriMatcher.match(uri);
        String tableName = null;
        switch (code) {
            case BOOK_URI_CODE:
                tableName = DbOpenHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        return tableName;
    }


    @Override
    public void localLog(String msg) {
        Log.d(TAG, msg);
    }
}
