// ITinywlInput.aidl
package tinywl;

// Declare any non-default types here with import statements
import com.android.server.inputflinger.KeyEvent;
import android.hardware.input.common.MotionEvent;

interface ITinywlInput {
    boolean onKeyEvent(in KeyEvent event, long nativePtr);
    boolean onMotionEvent(in MotionEvent event, long nativePtr);
}
