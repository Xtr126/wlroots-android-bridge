// ITinywlCallback.aidl
package com.xtr.tinywl;

// Declare any non-default types here with import statements

interface ITinywlCallback {
    void onSurfaceCreated(in Surface surface);
    void onSurfaceChanged(in Surface surface);
}