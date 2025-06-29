// ITinywlCallback.aidl
package com.xtr.compound;

// Declare any non-default types here with import statements
import android.view.Surface;

interface ITinywlCallback {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onSurfaceCreated(in Surface surface);
}