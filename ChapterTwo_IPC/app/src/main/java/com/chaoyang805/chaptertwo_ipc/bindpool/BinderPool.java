package com.chaoyang805.chaptertwo_ipc.bindpool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.chaoyang805.chaptertwo_ipc.Debugable;

import java.util.concurrent.CountDownLatch;

/**
 * Created by chaoyang805 on 16/7/29.
 */

public class BinderPool implements Debugable {

    public static final int BINDER_NONE = -1;
    public static final int BINDER_SECURITY_CENTER = 1;
    public static final int BINDER_COMPUTE = 2;
    private static volatile BinderPool sInstance;

    private IBinderPool mBinderPool;
    private Context mContext;

    private CountDownLatch mConnectBinderPoolCountDownLatch;
    private ServiceConnection mBinderPoolConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            localLog("binderpool service connected" + Thread.currentThread().getName());
            mBinderPool = IBinderPool.Stub.asInterface(iBinder);
            try {
                mBinderPool.asBinder().linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mConnectBinderPoolCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            mBinderPool.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mDeathRecipient = null;
            connectBinderPoolService();
        }
    };

    private BinderPool(Context context) {
        mContext = context.getApplicationContext();
        connectBinderPoolService();
    }

    private synchronized void connectBinderPoolService() {
        mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
        mContext.bindService(new Intent(mContext, BinderPoolService.class), mBinderPoolConnection, Context.BIND_AUTO_CREATE);
        localLog("connecting to binderpool service " + Thread.currentThread().getName());
        try {
            mConnectBinderPoolCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        localLog("connected");
    }

    public static BinderPool getInstance(Context context) {
        if (sInstance == null) {
            synchronized (BinderPool.class) {
                if (sInstance == null) {
                    sInstance = new BinderPool(context);
                }
            }
        }
        return sInstance;
    }

    public IBinder queryBinder(int binderCode) {
        IBinder binder = null;
        if (mBinderPool != null) {
            try {
                binder = mBinderPool.queryBinder(binderCode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return binder;

    }


    /**
     * BinderPoolImpl.class
     */
    public static class BinderPoolImpl extends IBinderPool.Stub {
        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode) {
                case BINDER_COMPUTE:
                    binder = new ComputeImpl();
                    break;
                case BINDER_SECURITY_CENTER:
                    binder = new SecurityCenterImpl();
                    break;
                default:
                    break;
            }
            return binder;
        }

    }

    @Override
    public void localLog(String msg) {
        final String TAG = "BinderPool";
        Log.d(TAG, msg);
    }
}
