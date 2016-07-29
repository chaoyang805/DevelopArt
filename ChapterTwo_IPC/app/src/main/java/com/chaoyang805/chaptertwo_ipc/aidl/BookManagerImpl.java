package com.chaoyang805.chaptertwo_ipc.aidl;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.v4.app.BundleCompat;
import android.util.Log;

import com.chaoyang805.chaptertwo_ipc.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaoyang805 on 16/7/25.
 */

public class BookManagerImpl extends Binder implements IBookManager {
    private static final String TAG = "BookManagerImpl";
    private List<Book> mBooks;
    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList;

    public BookManagerImpl(List<Book> books, RemoteCallbackList<IOnNewBookArrivedListener> listeners) {
        this();
        mBooks = books;
        mListenerList = listeners;

    }

    public BookManagerImpl() {
        this.attachInterface(this, DESCRIPTOR);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    public static IBookManager asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
        if (iin != null && iin instanceof IBookManager) {
            return (IBookManager) iin;
        }
        return new BookManagerImpl.Proxy(obj);
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION:{
                reply.writeString(DESCRIPTOR);
                return true;
            }
            case TRANSACTION_getBookList:{

                data.enforceInterface(DESCRIPTOR);
                List<Book> bookList = this.getBookList();
                reply.writeNoException();
                reply.writeTypedList(bookList);
                return true;
            }
            case TRANSACTION_addBook: {

                data.enforceInterface(DESCRIPTOR);
                Book arg0;
                if (0 != data.readInt()) {
                    arg0 = Book.CREATOR.createFromParcel(data);
                } else {
                    arg0 = null;
                }
                this.addBook(arg0);
                reply.writeNoException();
                return true;
            }
            case TRANSACTION_registerListener:{

                data.enforceInterface(DESCRIPTOR);
                IOnNewBookArrivedListener arg0;
                arg0 = IOnNewBookArrivedListenerImpl.asInterface(data.readStrongBinder());
                this.registerListener(arg0);
                reply.writeNoException();
                return true;
            }
            case TRANSACTION_unregisterListener: {
                IOnNewBookArrivedListener arg0;
                data.enforceInterface(DESCRIPTOR);
                arg0 = IOnNewBookArrivedListenerImpl.asInterface(data.readStrongBinder());
                this.unregisterListener(arg0);
                reply.writeNoException();
                return true;
            }
        }
        return super.onTransact(code, data, reply, flags);
    }

    @Override
    public List<Book> getBookList() {
        return mBooks;
    }

    @Override
    public void addBook(Book book) {
        if (mBooks != null) {
            mBooks.add(book);
        }
    }

    @Override
    public void registerListener(IOnNewBookArrivedListener listener) {
        mListenerList.register(listener);
    }

    @Override
    public void unregisterListener(IOnNewBookArrivedListener listener) {
        mListenerList.unregister(listener);
    }

    private static class Proxy implements IBookManager {

        private IBinder mRemote;

        public Proxy(IBinder remote) {
            mRemote = remote;
        }

        public String getInterfaceDescriptor() {
            return DESCRIPTOR;
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            List<Book> result;
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                mRemote.transact(TRANSACTION_getBookList, data, reply, 0);
                reply.readException();
                result = reply.createTypedArrayList(Book.CREATOR);
            } finally {
                data.recycle();
                reply.recycle();
            }
            return result;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                if (book != null) {
                    data.writeInt(1);
                    book.writeToParcel(data, 0);
                } else {
                    data.writeInt(0);
                }
                mRemote.transact(TRANSACTION_addBook, data, reply, 0);
                reply.readException();

            } finally {
                data.recycle();
                reply.recycle();
            }
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try{
                data.writeInterfaceToken(DESCRIPTOR);
                data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                mRemote.transact(TRANSACTION_registerListener, data, reply, 0);
                reply.readException();
            } finally {
                data.recycle();
                reply.recycle();
            }


        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                mRemote.transact(TRANSACTION_unregisterListener, data, reply, 0);
                reply.readException();
            } finally {
                data.recycle();
                reply.recycle();
            }
        }

        @Override
        public IBinder asBinder() {
            return mRemote;
        }
    }

}
