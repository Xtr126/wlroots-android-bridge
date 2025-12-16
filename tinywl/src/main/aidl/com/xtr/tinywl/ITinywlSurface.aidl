// ITinywlCallback.aidl
package com.xtr.tinywl;

// Declare any non-default types here with import statements
import com.xtr.tinywl.XdgTopLevel;
import android.view.Surface;

interface ITinywlSurface {
    void onSurfaceCreated(in XdgTopLevel xdgToplevel, in Surface surface);
    void onSurfaceChanged(in XdgTopLevel xdgToplevel, in Surface surface);
    void onSurfaceDestroyed(in XdgTopLevel xdgToplevel);
}