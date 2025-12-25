// ITinywlCallback.aidl
package com.xtr.tinywl;

// Declare any non-default types here with import statements
import com.xtr.tinywl.NativePtrType;
import android.view.Surface;

interface ITinywlSurface {
    void onSurfaceCreated(long nativePtr, in NativePtrType nativePtrType, in Surface surface);
    void onSurfaceChanged(long nativePtr, in NativePtrType nativePtrType, in Surface surface);
    void onSurfaceDestroyed(long nativePtr, in NativePtrType nativePtrType);
}