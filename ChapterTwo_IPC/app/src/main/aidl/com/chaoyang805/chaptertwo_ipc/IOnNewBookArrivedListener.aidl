// IOnNewBokkArrivedListener.aidl
package com.chaoyang805.chaptertwo_ipc;

// Declare any non-default types here with import statements
import com.chaoyang805.chaptertwo_ipc.Book;

interface IOnNewBookArrivedListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onNewBookArrived(in Book book);
}
