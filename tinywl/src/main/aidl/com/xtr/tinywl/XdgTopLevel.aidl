// XdgTopLevel.aidl
package com.xtr.tinywl;

// Declare any non-default types here with import statements
import com.xtr.tinywl.NativePtrType;

parcelable XdgTopLevel {
    String appId;
    String title;
    NativePtrType nativePtrType;
    long nativePtr;
}