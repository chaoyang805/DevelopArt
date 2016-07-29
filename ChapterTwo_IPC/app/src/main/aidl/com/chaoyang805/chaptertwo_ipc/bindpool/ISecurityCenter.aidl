// ISecurityCenter.aidl
package com.chaoyang805.chaptertwo_ipc.bindpool;

// Declare any non-default types here with import statements

interface ISecurityCenter {

    String encrypt(String content);
    String decrypt(String password);
}
