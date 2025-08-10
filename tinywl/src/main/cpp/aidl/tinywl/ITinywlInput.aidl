// ITinywlInput.aidl
package tinywl;

// Declare any non-default types here with import statements
import com.android.server.inputflinger.KeyEvent;
import android.hardware.input.common.MotionEvent;

interface ITinywlInput {
    boolean onKeyEvent(in KeyEvent event);
    boolean onMotionEvent(in MotionEvent event);
}
