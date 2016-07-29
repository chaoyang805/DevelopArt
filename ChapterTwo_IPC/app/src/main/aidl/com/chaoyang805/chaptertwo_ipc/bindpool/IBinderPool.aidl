// IBinderPool.aidl
package com.chaoyang805.chaptertwo_ipc.bindpool;

// Declare any non-default types here with import statements

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
