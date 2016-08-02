package com.chaoyang805.chaptertwo_ipc.bindpool;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.chaoyang805.chaptertwo_ipc.Debugable;
import com.chaoyang805.chaptertwo_ipc.R;

/**
 * Created by chaoyang805 on 16/7/29.
 */

public class BinderPoolActivity extends AppCompatActivity implements Debugable {

    private ISecurityCenter mSecurityCenter;

    private ICompute mCompute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binderpool);
        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
            }
        }).start();
    }

    private void doWork() {
        BinderPool binderPool = BinderPool.getInstance(BinderPoolActivity.this);
        IBinder securityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
        mSecurityCenter = SecurityCenterImpl.asInterface(securityBinder);
        localLog("Access Security Center");
        String msg = "Hello World Android";
        try {
            String password = mSecurityCenter.encrypt(msg);
            localLog("encrypt: " + password);
            localLog("decrypt: " + mSecurityCenter.decrypt(password));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        IBinder computeBinder = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
        mCompute = ComputeImpl.asInterface(computeBinder);
        localLog("access compute ");

        try {
            int result = mCompute.add(1, 4);
            localLog("result is:" + result);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void localLog(String msg) {
        final String TAG = "BinderPoolActivity";
        Log.d(TAG, msg);
    }

}
