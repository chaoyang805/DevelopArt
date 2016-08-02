package com.chaoyang805.chaptertwo_ipc.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.chaoyang805.chaptertwo_ipc.Book;
import com.chaoyang805.chaptertwo_ipc.IBookManager;
import com.chaoyang805.chaptertwo_ipc.aidl.BookManagerImpl;
import com.chaoyang805.chaptertwo_ipc.aidl.IOnNewBookArrivedListener;
import com.chaoyang805.chaptertwo_ipc.aidl.IOnNewBookArrivedListenerImpl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by chaoyang805 on 16/7/26.
 */
public class BookManagerService extends Service {

    private CopyOnWriteArrayList<Book> mBooks;
    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new RemoteCallbackList<>();

    private BookManagerImpl mBinder;
    private AtomicBoolean mIsServiceDead = new AtomicBoolean(false);

    @Override
    public void onCreate() {
        super.onCreate();
        mBooks = new CopyOnWriteArrayList<>();
        mBooks.add(new Book(1, "Android"));
        mBooks.add(new Book(2, "iOS"));
        new Thread(new ServiceWorker()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mBinder = new BookManagerImpl(mBooks, mListenerList);
        return mBinder;
    }

    @Override
    public void onDestroy() {
        mIsServiceDead.set(true);
        super.onDestroy();
    }

    private void onNewBookArrived(Book book) throws RemoteException {
        mBooks.add(book);
        final int N = mListenerList.beginBroadcast();

        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener broadcastItem = mListenerList.getBroadcastItem(i);
            if (broadcastItem != null) {
                broadcastItem.onNewBookArrived(book);
            }
        }

        mListenerList.finishBroadcast();
    }

    private class ServiceWorker implements Runnable {

        @Override
        public void run() {
            while (!mIsServiceDead.get()) {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int bookId = mBooks.size() + 1;

                Book newBook = new Book(bookId, "bookName#" + bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
