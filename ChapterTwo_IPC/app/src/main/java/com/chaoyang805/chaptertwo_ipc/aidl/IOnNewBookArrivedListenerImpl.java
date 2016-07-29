package com.chaoyang805.chaptertwo_ipc.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import com.chaoyang805.chaptertwo_ipc.Book;

/**
 * Created by chaoyang805 on 16/7/26.
 */

public abstract class IOnNewBookArrivedListenerImpl extends Binder implements IOnNewBookArrivedListener {
    @Override
    public IBinder asBinder() {
        return this;
    }

    public static IOnNewBookArrivedListener asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
        if (iin != null && iin instanceof IOnNewBookArrivedListener) {
            return (IOnNewBookArrivedListener) iin;
        }
        return new IOnNewBookArrivedListenerImpl.Proxy(obj);
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION:
                data.writeString(DESCRIPTOR);
                return true;
            case TRANSACTION_onNewBookArrived:
                data.enforceInterface(DESCRIPTOR);
                Book arg0;
                if (0 != data.readInt()) {
                    arg0 = Book.CREATOR.createFromParcel(data);
                } else {
                    arg0 = null;
                }
                this.onNewBookArrived(arg0);
                reply.writeNoException();
                return true;
        }
        return super.onTransact(code, data, reply, flags);
    }

    private static class Proxy implements IOnNewBookArrivedListener {
        private IBinder mRemote;

        public Proxy(IBinder remote) {
            mRemote = remote;
        }

        public String getInterfaceDescriptor() {
            return DESCRIPTOR;
        }

        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
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
                mRemote.transact(TRANSACTION_onNewBookArrived, data, reply, 0);
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
