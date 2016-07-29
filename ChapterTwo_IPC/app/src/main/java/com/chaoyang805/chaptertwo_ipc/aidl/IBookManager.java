package com.chaoyang805.chaptertwo_ipc.aidl;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import com.chaoyang805.chaptertwo_ipc.Book;

import java.util.List;

/**
 * Created by chaoyang805 on 16/7/25.
 */
public interface IBookManager extends IInterface {

    static final String DESCRIPTOR = "com.chaoyang805.chaptertwo_ipc.aidl.IBookManager";
    static final int TRANSACTION_getBookList = IBinder.FIRST_CALL_TRANSACTION + 0;
    static final int TRANSACTION_addBook = IBinder.FIRST_CALL_TRANSACTION + 1;
    static final int TRANSACTION_registerListener = IBinder.FIRST_CALL_TRANSACTION + 2;
    static final int TRANSACTION_unregisterListener = IBinder.FIRST_CALL_TRANSACTION + 3;

    public List<Book> getBookList() throws RemoteException;

    public void addBook(Book book) throws RemoteException;
    public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException;
    public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException;
}
