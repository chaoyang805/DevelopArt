package com.chaoyang805.chaptertwo_ipc.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.chaoyang805.chaptertwo_ipc.Book;
import com.chaoyang805.chaptertwo_ipc.R;
import com.chaoyang805.chaptertwo_ipc.aidl.BookManagerImpl;
import com.chaoyang805.chaptertwo_ipc.aidl.IBookManager;
import com.chaoyang805.chaptertwo_ipc.aidl.IOnNewBookArrivedListenerImpl;
import com.chaoyang805.chaptertwo_ipc.service.BookManagerService;

import java.util.List;

/**
 * Created by chaoyang805 on 16/7/26.
 */
public class BookManagerActivity extends AppCompatActivity {
    private static final String TAG = "BookManagerActivity";
    private IOnNewBookArrivedListenerImpl mListener = new IOnNewBookArrivedListenerImpl() {
        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            Log.d(TAG, "onNewBookArrived: " + book.bookName);
        }
    };

    private IBookManager mBookManager;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IBookManager manager = BookManagerImpl.asInterface(iBinder);
            mBookManager = manager;
            try {
                mBookManager.registerListener(mListener);
                List<Book> books = manager.getBookList();
                Log.d(TAG, "query book list, list type:" + books.getClass().getCanonicalName());
                Log.d(TAG, "query book list:" + books.get(0).bookName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmanager);
        bindService(new Intent(this, BookManagerService.class),mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        if (mBookManager != null && mBookManager.asBinder().isBinderAlive()) {
            try {
                Log.d(TAG, "unregisterlistener:" + mListener);
                mBookManager.unregisterListener(mListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
        super.onDestroy();
    }
}
