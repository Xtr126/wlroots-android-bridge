// ITinywlCallback.aidl
package com.xtr.compound;

// Declare any non-default types here with import statements
import android.view.Surface;
import com.android.server.inputflinger.KeyEvent;
import android.hardware.input.common.MotionEvent;

interface ITinywlCallback {
    void onSurfaceCreated(in Surface surface);
    void onKeyEvent(in KeyEvent event);
    void onMotionEvent(in MotionEvent event);
}