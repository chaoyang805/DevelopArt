package com.chaoyang805.chaptertwo_ipc.bindpool;

import android.os.RemoteException;

/**
 * Created by chaoyang805 on 16/7/29.
 */

public class ComputeImpl extends ICompute.Stub {

    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
