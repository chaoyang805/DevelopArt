package com.chaoyang805.chaptertwo_ipc.bindpool;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.chaoyang805.chaptertwo_ipc.Debugable;

/**
 * Created by chaoyang805 on 16/7/29.
 */

public class BinderPoolService extends Service implements Debugable {
    // 服务器端的binder
    private Binder mBinderPool = new BinderPool.BinderPoolImpl();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        localLog("onBind");
        return mBinderPool;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void localLog(String msg) {
        final String TAG = "BinderPoolService";
        Log.d(TAG, msg);
    }
}
