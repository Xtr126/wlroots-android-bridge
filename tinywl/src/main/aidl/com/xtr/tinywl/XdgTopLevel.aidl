// XdgTopLevel.aidl
package com.xtr.tinywl;

// Declare any non-default types here with import statements

parcelable XdgTopLevel {
    enum NativePtrType {
        VIEW,
        OUTPUT,
    }
    String appId;
    String title;
    NativePtrType nativePtrType;
    long nativePtr;
}