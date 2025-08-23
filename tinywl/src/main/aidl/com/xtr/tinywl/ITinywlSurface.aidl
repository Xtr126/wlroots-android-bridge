// ITinywlCallback.aidl
package com.xtr.tinywl;

// Declare any non-default types here with import statements
import com.xtr.tinywl.XdgTopLevel;

interface ITinywlSurface {
    void onSurfaceCreated(in XdgTopLevel xdgToplevel);
    void onSurfaceChanged(in XdgTopLevel xdgToplevel);
    void onSurfaceDestroyed(in XdgTopLevel xdgToplevel);
}