#include <jni.h>
// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("tinywl");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("tinywl")
//      }
//    }

extern "C"
JNIEXPORT int JNICALL
Java_com_xtr_compound_Tinywl_onSurfaceCreated(JNIEnv *env, jclass clazz, jobject surface,
                                              jobject input_transfer_token) {
    // Cast nativePtr back to your compositor's context
    // Get ANativeWindow from jSurface
//    ANativeWindow* android_native_window = ANativeWindow_fromSurface(env, jSurface);
    // Store this android_native_window pointer for this toplevel
    // You'll then use ANativeWindow_lock/unlockAndPost with AHardwareBuffer
}