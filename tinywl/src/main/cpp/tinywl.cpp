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
JNIEXPORT jobject JNICALL
Java_com_xtr_tinywl_Tinywl_nativeGetTinywlInputServiceBinder(JNIEnv *env, jclass clazz) {
    // TODO: implement nativeGetTinywlInputServiceBinder()
}
extern "C"
JNIEXPORT jobject JNICALL
Java_com_xtr_tinywl_Tinywl_nativeGetTinywlSurfaceBinder(JNIEnv *env, jclass clazz) {
    // TODO: implement nativeGetTinywlSurfaceBinder()
}
extern "C"
JNIEXPORT void JNICALL
Java_com_xtr_tinywl_Tinywl_nativeRegisterXdgTopLevelCallback(JNIEnv *env, jclass clazz,
                                                             jobject binder) {
    // TODO: implement nativeRegisterXdgTopLevelCallback()
}
extern "C"
JNIEXPORT void JNICALL
Java_com_xtr_tinywl_Tinywl_runTinywlLoop(JNIEnv *env, jclass clazz, jobjectArray args) {
    // TODO: implement runTinywlLoop()
}