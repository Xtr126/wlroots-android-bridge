// XdgTopLevel.aidl
package com.xtr.tinywl;

// Declare any non-default types here with import statements
import android.view.Surface;

parcelable XdgTopLevel {
    String appId;
    String title;
    Surface surface;
    long nativePtr;
}