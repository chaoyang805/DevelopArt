package com.chaoyang805.chaptertwo_ipc.aidl;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import com.chaoyang805.chaptertwo_ipc.Book;

/**
 * Created by chaoyang805 on 16/7/26.
 */

public interface IOnNewBookArrivedListener extends IInterface {
    static final String DESCRIPTOR =
        "com.chaoyang805.chaptertwo_ipc.aidl.IOnNewBookArrivedListener";
    static final int TRANSACTION_onNewBookArrived = IBinder.FIRST_CALL_TRANSACTION + 2;

    public void onNewBookArrived(Book book) throws RemoteException;
}
