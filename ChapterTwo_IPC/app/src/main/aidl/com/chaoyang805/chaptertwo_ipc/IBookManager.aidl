// IBookManager.aidl
package com.chaoyang805.chaptertwo_ipc;

// Declare any non-default types here with import statements
import com.chaoyang805.chaptertwo_ipc.Book;
import com.chaoyang805.chaptertwo_ipc.IOnNewBookArrivedListener;
interface IBookManager {

    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}
