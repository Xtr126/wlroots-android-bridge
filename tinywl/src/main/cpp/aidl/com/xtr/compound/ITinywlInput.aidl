// ITinywlInput.aidl
package com.xtr.compound;

// Declare any non-default types here with import statements
import com.android.server.inputflinger.KeyEvent;
import android.hardware.input.common.MotionEvent;

interface ITinywlInput {
    void onKeyEvent(in KeyEvent event);
    void onMotionEvent(in MotionEvent event);
}
